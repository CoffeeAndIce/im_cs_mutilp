package channelHandle;

import com.alibaba.fastjson.JSONObject;
import dataHandle.emun.ChatFlag;
import dataHandle.service.UserInfoService;
import initApplication.SpringInit;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import pojo.MsgContent;
import pojo.UserInfo;
import utils.RedisUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Coffeeanice
 * @Description: TODO: 消息文本处理服务类
 * @Date: 2019/2/27 10:47
 */

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private UserInfoService userInfoService;

    private final ChannelGroup group;
    private static HashMap<String, String> idList = new HashMap<>();
    private static String[] notHererReason = {"亲，该用户暂时不在线哦!（您发送的消息将会在对方登陆后收到）", "亲，请稍后在试，该用户不在线哦（但您发送的消息将会在对方登陆后收到）", "亲，请换一个时间段再试，用户不在线哦（您发送的消息将会在对方登陆后收到）"};
    private static Random random = new Random();

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
        this.userInfoService = SpringInit.getBean(UserInfoService.class);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //是否握手成功，升级为 Websocket 协议
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 握手成功，移除 HttpRequestHandler，因此将不会接收到任何消息
            // 并把握手成功的 Channel 加入到 ChannelGroup 中
//            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            group.add(ctx.channel());
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent.state() == IdleState.READER_IDLE) {
                group.remove(ctx.channel());
                ctx.close().addListener(ChannelFutureListener.CLOSE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //增加消息的引用计数（保留消息），并将他写到 ChannelGroup 中所有已经连接的客户端
        Channel channel = ctx.channel();
        try {
            String text = msg.text();
            MsgContent msgContent = JSONObject.parseObject(text, MsgContent.class);
            String userId = msgContent.getUserId();
            String sellerId = msgContent.getSellerId();
            String channelId = channel.id().asLongText();
            if (StringUtils.isBlank(userId)) {
                group.remove(channel);
                ctx.writeAndFlush(new TextWebSocketFrame("请登录后再进行操作！")).addListener(ChannelFutureListener.CLOSE);
            } else {
                RedisUtil.getHashMethod().hset("CHAT_ALIVE", userId, channelId);
                idList.put(channelId, userId);//补充内容
                String msgs = msgContent.getMsg();
                if (ChatFlag.HEART_FLAG.equals(msgs)) {
                    //初始化用户信息
                    /**
                     * 简略判断，根据SellerId是否需要加载历史好友列表，返回的数据类型也不一致
                     */
                    _userChannelAdaptor(msgContent, userId, sellerId);
                    msgContent.setMsgType(1);
                    msgContent.setOnRead(1);
                    msgContent.setMsg("连接成功");
                    ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(msgContent)));
                } else {
                    String chat_alive = RedisUtil.getHashMethod().hget(ChatFlag.ALIVE_LIST, sellerId);
                    //用于发送信息给指定信道的人
                    if (StringUtils.isNotBlank(chat_alive)) {
                        Iterator<Channel> iterator = group.iterator();
                        Boolean founded = Boolean.FALSE;
                        while (iterator.hasNext() && !founded) {
                            Channel next = iterator.next();
                            if (chat_alive.equals(next.id().asLongText())) {
                                String infoCache = RedisUtil.getHashMethod().hget(ChatFlag.CHAT_RELATE + sellerId, ChatFlag.USER_INFO);
                                if (StringUtils.isBlank(infoCache)) {
                                    ctx.writeAndFlush(new TextWebSocketFrame(notHererReason[random.nextInt(notHererReason.length)]));
                                    break;
                                } else {
                                    String selfInfoCache = RedisUtil.getHashMethod().hget(ChatFlag.CHAT_RELATE + userId, ChatFlag.USER_INFO);
                                    //========存储联系关系================
                                    _saveRelation(userId, sellerId, infoCache);
                                    //消息记录缓存
                                    _saveCommucationReCode(text, userId, sellerId);
                                    //对话的用户
                                    msgContent.setData(selfInfoCache);
                                    msgContent.setMsgType(2);
                                    msgContent.setOnRead(1);
                                    next.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(msgContent)).retain());
                                    founded = true;
                                }
                            }
                        }
                        if (!founded) {
                            _relationAndOfflineHandler(ctx, msgContent, userId, sellerId);
                        }

                    } else {
                        _relationAndOfflineHandler(ctx, msgContent, userId, sellerId);
                    }

                }
            }

        } catch (RuntimeException e) {
            e.getCause();
            ctx.disconnect();
        }
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        idList.put(ctx.channel().id().asLongText(), "");//初始化数据
        System.out.println(idList);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        StringBuilder sb = new StringBuilder("用户下线：");
        String userId = idList.get(ctx.channel().id().asLongText());

        _onLineTips(userId,4);

        RedisUtil.getHashMethod().hdel("CHAT_ALIVE", userId);
        idList.remove(ctx.channel().id().asLongText());//清除下线用户
        System.out.println(sb.append(ctx.channel().id().asLongText()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    /**
     * @Author: Coffeeanice
     * @Description: TODO: 用于处理前台和后台的用户伪心跳初始化
     * @Date: 2019/3/29 10:31
     */
    private void _userChannelAdaptor(MsgContent msgContent, String userId, String sellerId) {
        Map<String, String> onlineList = RedisUtil.getHashMethod().hgetAll(ChatFlag.ALIVE_LIST);
        //后台管理用户
        if (StringUtils.isBlank(sellerId)) {
            //可以用于删除作为更新用户信息用
            String infoCache = RedisUtil.getHashMethod().hget(ChatFlag.CHAT_RELATE + userId, ChatFlag.USER_INFO);
            if (StringUtils.isBlank(infoCache)) {
                UserInfo accountByUserId = userInfoService.findAccountByUserId(userId);
                RedisUtil.getHashMethod().hset(ChatFlag.CHAT_RELATE + userId, ChatFlag.USER_INFO, JSONObject.toJSONString(accountByUserId));
            }
            Map<String, String> stringStringMap = RedisUtil.getHashMethod().hgetAll(ChatFlag.CHAT_RELATE + userId);
            msgContent.setRelateNet(JSONObject.toJSONString(stringStringMap));

            //离线信息
            Map<String, String> outLineMsg = RedisUtil.getHashMethod().hgetAll(ChatFlag.OUT_LINE + userId);
            msgContent.setOffLine(JSONObject.toJSONString(outLineMsg));
            RedisUtil.getHashMethod().hdel(ChatFlag.OUT_LINE + userId);

        } else {
            //主动连接，将对方基本通讯信息获取
            UserInfo accountByUserId = userInfoService.findAccountByUserId(sellerId);
            UserInfo selfInfo = userInfoService.findAccountByUserId(userId);
            RedisUtil.getHashMethod().hset(ChatFlag.CHAT_RELATE + accountByUserId.getUserId(), ChatFlag.USER_INFO, JSONObject.toJSONString(accountByUserId));
            RedisUtil.getHashMethod().hset(ChatFlag.CHAT_RELATE + selfInfo.getUserId(), ChatFlag.USER_INFO, JSONObject.toJSONString(selfInfo));

            //关系网获取
            Map<String, String> stringStringMap = RedisUtil.getHashMethod().hgetAll(ChatFlag.CHAT_RELATE + userId);
            msgContent.setRelateNet(JSONObject.toJSONString(stringStringMap));

            msgContent.setData(JSONObject.toJSONString(accountByUserId));

            //离线信息
            Map<String, String> outLineMsg = RedisUtil.getHashMethod().hgetAll(ChatFlag.OUT_LINE + userId);
            msgContent.setOffLine(JSONObject.toJSONString(outLineMsg));
            RedisUtil.getHashMethod().hdel(ChatFlag.OUT_LINE + userId);
        }
        msgContent.setOnLine(JSONObject.toJSONString(onlineList));
        
        _onLineTips(userId,3);
    }

    /**
     * @Author: Coffeeanice
     * @Description: TODO: 用于广播传输上线信息
     * @Date: 2019/4/1 11:38
     * @param type 消息类型  1= 初始化心跳 2 = 普通消息 3 = 上线信息 4 = 离线信息 5 = 系统广播消息
     */
    private void _onLineTips(String userId,int type) {
        String lineCache = RedisUtil.getHashMethod().hget(ChatFlag.CHAT_RELATE + userId, ChatFlag.USER_INFO);
        if (StringUtils.isNotBlank(lineCache)) {
            MsgContent onLineTips = new MsgContent();
            onLineTips.setMsgType(type);
            onLineTips.setData(lineCache);
            group.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(onLineTips)));
        }
    }

    /**
     * @Author: Coffeeanice
     * @Description: TODO: 用于处理离线时关系网构建与消息存储问题（含离线）
     * @Date: 2019/3/29 10:27
     */
    private void _relationAndOfflineHandler(ChannelHandlerContext ctx, MsgContent text, String userId, String sellerId) {
        text.setOnRead(2);
        text.setMsgType(4);
        String cacheMsg = JSONObject.toJSONString(text);
        //关系网维护
        String infoCache = RedisUtil.getHashMethod().hget(ChatFlag.CHAT_RELATE + sellerId, ChatFlag.USER_INFO);
        _saveRelation(userId, sellerId, infoCache);
        //消息记录缓存
        _saveCommucationReCode(cacheMsg, userId, sellerId);
        //没有信道，保存离线信息
        RedisUtil.getHashMethod().hset(ChatFlag.OUT_LINE + sellerId, LocalDateTime.now().withNano(0).toString(), cacheMsg);
        ctx.writeAndFlush(new TextWebSocketFrame(notHererReason[random.nextInt(notHererReason.length)]));
    }

    /**
     * @Author: Coffeeanice
     * @Description: TODO: 用于存储聊天记录
     * @Date: 2019/3/28 17:02
     */
    private void _saveCommucationReCode(String text, String userId, String sellerId) {
        //己方对话保存
        RedisUtil.getHashMethod().hset(ChatFlag.CHAT_MSG + userId + ":" + sellerId, LocalDateTime.now().withNano(0).toString(), text);
        //对方对话保存
        RedisUtil.getHashMethod().hset(ChatFlag.CHAT_MSG + sellerId + ":" + userId, LocalDateTime.now().withNano(0).toString(), text);
    }

    /**
     * @Author: Coffeeanice
     * @Description: TODO: 用于存储关系网
     * @Date: 2019/3/28 17:00
     */
    private void _saveRelation(String userId, String sellerId, String infoCache) {
        String selfInfoCache = RedisUtil.getHashMethod().hget(ChatFlag.CHAT_RELATE + userId, ChatFlag.USER_INFO);
        //========存储联系关系================
        if (!userId.equals(sellerId)) {
            //存储自己的关系,不回复不会建立关系
            RedisUtil.getHashMethod().hset(ChatFlag.CHAT_RELATE + userId, sellerId, infoCache);
            RedisUtil.getHashMethod().hset(ChatFlag.CHAT_RELATE + sellerId, userId, selfInfoCache);
        }
    }
}

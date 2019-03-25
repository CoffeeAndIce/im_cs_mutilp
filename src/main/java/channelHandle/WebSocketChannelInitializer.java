package channelHandle;

import dataHandle.emun.ChatFlag;
import dataHandle.service.UserInfoService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *  @Author:      Coffeeanice
 * @Description:   TODO: Socket 对接服务初始化类
 * @Date:     2019/2/27 11:00
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelGroup group;

    public WebSocketChannelInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //HttpServerCodec: 针对http协议进行编解码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        //ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
        pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
        /**
         * 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
         * 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
         */
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(8192));

        //用于处理websocket, /ws为访问websocket时的uri
        pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));

        // //当连接在60秒内没有接收到消息时，进会触发一个 IdleStateEvent 事件，被 HeartbeatHandler 的 userEventTriggered 方法处理
        pipeline.addLast(new IdleStateHandler(ChatFlag.READ_IDLE_TIME_OUT, ChatFlag.WRITE_IDLE_TIME_OUT, ChatFlag.ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));
        //用于文字处理及心跳联测
        pipeline.addLast("textSocketHandler", new TextWebSocketFrameHandler(group));
    }
}
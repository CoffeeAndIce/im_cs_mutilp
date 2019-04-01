package pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Coffeeanice
 * @Description: TODO: 消息体属性
 * @Date: 2019/2/27 16:08
 */
public class MsgContent implements Serializable {
    /**
     * 在线用户信息获取
     */
    private String onLine;

    /**
     * 是否已读  1 = 已读，2 = 未读
     */
    private Integer onRead;
    /**
     * 消息类型  1= 初始化心跳 2 = 普通消息 3 = 上线信息 4 = 离线信息 5 = 系统广播消息
     */
    private Integer msgType;
    /**
     * 消息内容
     */
    private String msg;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 商户id(权当另一个用户Id)
     */
    private String sellerId;

    /**
     * 发送时间
     */
    private Date time;

    /**
     * 额外数据
     * 对于前台用户，是获取当前针对商家的信息
     */
    private String data;

    /**
     * 用于获取用户关系表的数据
     */
    private String relateNet;
    /**
     * 离线数据
     */
    private String offLine;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getOffLine() {
        return offLine;
    }

    public void setOffLine(String offLine) {
        this.offLine = offLine;
    }

    public String getRelateNet() {
        return relateNet;
    }

    public void setRelateNet(String relateNet) {
        this.relateNet = relateNet;
    }

    public Integer getOnRead() {
        return onRead;
    }

    public void setOnRead(Integer onRead) {
        this.onRead = onRead;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }

    @Override
    public String toString() {
        return "MsgContent{" +
                "onLine='" + onLine + '\'' +
                ", onRead=" + onRead +
                ", msgType=" + msgType +
                ", msg='" + msg + '\'' +
                ", userId='" + userId + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", time=" + time +
                ", data='" + data + '\'' +
                ", relateNet='" + relateNet + '\'' +
                ", offLine='" + offLine + '\'' +
                '}';
    }
}

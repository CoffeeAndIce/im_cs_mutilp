package dataHandle.emun;

/**
 * @Author: Coffeeanice
 * @Description: TODO: 特定标识
 * 下述中的 userId 与SellerId 并没有确切定义，只是用于方便初始化时识别商户/用户的通讯而别名，通常商户用SellerId,普通用户用UserId
 * @Date: 2019/3/4 18:22
 */
public interface ChatFlag {
    /**
     * 读超时
     */
    public static final int READ_IDLE_TIME_OUT = 600;

    /**
     * 写超时
     */
    public static final int WRITE_IDLE_TIME_OUT = 0;

    /**
     * 所有超时
     */
    public static final int ALL_IDLE_TIME_OUT = 0;

    /**
     * 心跳标识
     */
    public static final String HEART_FLAG = "heart";

    /**
     * 存贮userId/sellerId 组合 对应在线的用户
     */
    public static final String ALIVE_LIST = "CHAT_ALIVE";

    /**
     * 后接 userId/sellerId  ，存储  key-时间 value-MsgContent 实体（msg,time,userId,sellerId）
     */
    public static final String CHAT_MSG = "CHAT_MSG";


    //=========================== 最高级键值 CHAT_RELATE ,是下方的父键值 =====================
    /**
     * 用于存放子键值的父级键值，用于表明关系
     */
    public static final String CHAT_RELATE = "CHAT_RELATE";

    /**
     * 用于存放当前用户本身信息，与userId/sellerId作为组合
     */
    public static final String USER_INFO = "INFO";

    //=========================== 离线信息模块 ============================
    /**
     * 用于存放离线信息   右方以MSG_用户的userId 为key ，此处的userId代表接收方的userId
     */
    public static final String OUT_LINE = "OUT_LINE";

}

package pojo;

import java.io.Serializable;
import java.util.Date;

/**
*  @Author:      Coffeeanice
* @Description:   TODO: 用于获取用户信息
* @Date:     2019/2/27 14:26
*/
public class UserInfo implements Serializable {
    /**
     * 商户类型 C:公司员工 S:销售商，W:物流商，Z:租赁商，G:工程队
     */
    private String merchantType;
    /**
     * 关联平台用户表主键
     */
    private String userId;

    /**
     * 登录用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String userNick;

    /**
     *商户公司名字
     */
    private String companyName;

    /**
     * 店铺名称
     */
    private String sellerName;

    /**
     * 商户门面图
     */
    private String sellerImg;

    /**
     * 用户头像
     */
    private String userAvar;

    /**
     * 账户分类
     */
    private Integer companyFlag;
    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户类别 0:只是普通用户, 1:销售商，2：物流商，3：租赁商
     */
    private Integer userType;

    /**
     * 0:表示为未申请，1：待审核，2：审核不通过,3:审核通过
     */
    private Integer auditStatus;

    /**
     * 关联平台商户端表德主键
     */
    private String sellerId;

    /**
     * 第3方登录 的qq_id
     */
    private String qqId;

    /**
     * 签名
     */
    private String signature;

    /**
     * qq_头像
     */
    private String qqImage;

    /**
     * wx_name
     */
    private String wxName;

    /**
     * wx_id
     */
    private String wxId;

    /**
     * wx_头像
     */
    private String wxImage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

    /**
     * 角色Id
     */
    private Integer roleId;

    /**
     * 是否可以进入首页
     * 1:是 2：否 (此处是表示是否为商家的员工)',
     */
    private Integer isEnter;

    /**
     * 状态 0禁用 1启用
     */
    private Integer state;

    public String getSellerImg() {
        return sellerImg;
    }

    public void setSellerImg(String sellerImg) {
        this.sellerImg = sellerImg;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserAvar() {
        return userAvar;
    }

    public void setUserAvar(String userAvar) {
        this.userAvar = userAvar;
    }

    public Integer getCompanyFlag() {
        return companyFlag;
    }

    public void setCompanyFlag(Integer companyFlag) {
        this.companyFlag = companyFlag;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getQqImage() {
        return qqImage;
    }

    public void setQqImage(String qqImage) {
        this.qqImage = qqImage;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getWxImage() {
        return wxImage;
    }

    public void setWxImage(String wxImage) {
        this.wxImage = wxImage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getIsEnter() {
        return isEnter;
    }

    public void setIsEnter(Integer isEnter) {
        this.isEnter = isEnter;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public String toString() {
        return new StringBuilder("UserInfo{" +
                "merchantType='" + merchantType + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userNick='" + userNick + '\'' +
                ", companyName='" + companyName + '\'' +
                ", userAvar='" + userAvar + '\'' +
                ", companyFlag=" + companyFlag +
                ", userPwd='" + userPwd + '\'' +
                ", userType=" + userType +
                ", auditStatus=" + auditStatus +
                ", sellerId='" + sellerId + '\'' +
                ", qqId='" + qqId + '\'' +
                ", signature='" + signature + '\'' +
                ", qqImage='" + qqImage + '\'' +
                ", wxName='" + wxName + '\'' +
                ", wxId='" + wxId + '\'' +
                ", wxImage='" + wxImage + '\'' +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", roleId=" + roleId +
                ", isEnter=" + isEnter +
                ", state=" + state +
                '}').toString();
    }
}

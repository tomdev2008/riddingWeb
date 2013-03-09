package com.ridding.meta;

import java.io.Serializable;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime2012-3-19 12:08:13 Class Description 个人信息meta
 */
public class Profile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2438650795381414280L;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 用户名
	 */
	private String userName = "";
	/**
	 * 昵称
	 */
	private String nickName = "";
	/**
	 * 小头像地址
	 */
	private String sAvatorUrl = "";
	/**
	 * 大头像地址
	 */
	private String bAvatorUrl = "";
	/**
	 * 小图的灰色图地址
	 */
	private String graysAvatorUrl = "";
	/**
	 * 总的骑行距离
	 */
	private int totalDistance;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 上次更新时间
	 */
	private long lastUpdateTime;
	/**
	 * 资源用户id，不存数据库
	 */
	private long accessUserId;
	/**
	 * 用户等级1为管理员
	 */
	private int level;
	/**
	 * 手机背景的地址
	 */
	private String backgroundUrl = "";
	/**
	 * 对应的sourceaccount
	 */
	private SourceAccount sourceAccount;
	/**
	 * 淘宝账号
	 */
	private String taobaoCode;

	public SourceAccount getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(SourceAccount sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public long getAccessUserId() {
		return accessUserId;
	}

	public void setAccessUserId(long accessUserId) {
		this.accessUserId = accessUserId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getsAvatorUrl() {
		return sAvatorUrl;
	}

	public void setsAvatorUrl(String sAvatorUrl) {
		this.sAvatorUrl = sAvatorUrl;
	}

	public String getbAvatorUrl() {
		return bAvatorUrl;
	}

	public void setbAvatorUrl(String bAvatorUrl) {
		this.bAvatorUrl = bAvatorUrl;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}

	public String getGraysAvatorUrl() {
		return graysAvatorUrl;
	}

	public void setGraysAvatorUrl(String graysAvatorUrl) {
		this.graysAvatorUrl = graysAvatorUrl;
	}

	public String getTaobaoCode() {
		return taobaoCode;
	}

	public void setTaobaoCode(String taobaoCode) {
		this.taobaoCode = taobaoCode;
	}

}

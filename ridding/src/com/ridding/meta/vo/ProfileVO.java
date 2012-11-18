package com.ridding.meta.vo;


/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-4-26 下午03:11:52 Class Description
 */
public class ProfileVO {
	/**
	 * 小头像地址
	 */
	private String sAvatorUrl;
	/**
	 * 大头像地址
	 */
	private String bAvatorUrl;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 用户权限
	 */
	private int userRole;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 新浪用户id
	 */
	private long accessUserId;

	private String nickName;

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getAccessUserId() {
		return accessUserId;
	}

	public void setAccessUserId(long accessUserId) {
		this.accessUserId = accessUserId;
	}
}

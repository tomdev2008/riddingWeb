package com.ridding.security;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-25 下午06:17:06 Class Description
 */
public class MyToken {
	/**
	 * 用户token
	 */
	private String token;
	/**
	 * 上次更新时候时间
	 */
	private long lastUpdateTime;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
}

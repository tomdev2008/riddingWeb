package com.ridding.meta;

import java.sql.Timestamp;

import javapns.devices.Device;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-1 下午06:34:05 Class Description apns TB_IosApns
 */
public class ApnsDevice implements Device {
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 对应token
	 */
	private String token;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 最后更新时间
	 */
	private long lastUpdateTime;

	/**
	 * 数据状态，0表示有效地，1表示无效的
	 */
	private int status;

	/**
	 * 有效的
	 */
	public static final int VALID = 1;

	/**
	 * 无效的
	 */
	public static final int INVALID = 0;

	@Override
	public String getDeviceId() {
		return this.token;
	}

	@Override
	public Timestamp getLastRegister() {
		return new Timestamp(lastUpdateTime);
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setDeviceId(String paramString) {
		this.token = paramString;
	}

	@Override
	public void setLastRegister(Timestamp paramTimestamp) {
		this.lastUpdateTime = paramTimestamp.getTime();
	}

	@Override
	public void setToken(String paramString) {
		this.token = paramString;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public boolean isState() {
		return this.status == ApnsDevice.VALID;
	}

	public void setStatus(boolean status) {
		this.status = status ? ApnsDevice.VALID : ApnsDevice.INVALID;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

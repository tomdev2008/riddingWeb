package com.ridding.meta;

import java.io.Serializable;

/**
 * 
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime2013-1-29 00:08:13 Class Description 附近用户信息meta
 */
public class UserNearby implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 809765774576L;
	/**
	 * 用户Id
	 * 
	 */
	private long userId;
	/**
	 * 纬度
	 * 
	 */
	private double latitude;
	/**
	 * 经度
	 * 
	 */
	private double longitude;
	/**
	 * Geohash值
	 */
	private String geohash;
	/**
	 * 上次更新时间
	 * 
	 */
	private long lastUpdateTime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
package com.ridding.meta.vo;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-9 下午08:32:43 Class Description
 */
public class Location {
	/**
	 * dwr传过来的经度
	 */
	private double latitude;
	/**
	 * dwr传过来的维度
	 */
	private double longtitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

}

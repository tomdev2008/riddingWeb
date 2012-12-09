package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-22 下午11:24:07 Class Description 地图偏移修复
 */
public class MapFix {

	private long id;
	/**
	 * 经度
	 */
	private double latitude;
	/**
	 * 纬度
	 */
	private double longtitude;
	/**
	 * x像素偏移
	 */
	private int offsetx;
	/**
	 * y像素偏移
	 */
	private int offsety;
	/**
	 * 偏移精度
	 */
	private double toLatitude;
	/**
	 * 偏移纬度
	 */
	private double toLongtitude;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public int getOffsetx() {
		return offsetx;
	}

	public void setOffsetx(int offsetx) {
		this.offsetx = offsetx;
	}

	public int getOffsety() {
		return offsety;
	}

	public void setOffsety(int offsety) {
		this.offsety = offsety;
	}

	public double getToLatitude() {
		return toLatitude;
	}

	public void setToLatitude(double toLatitude) {
		this.toLatitude = toLatitude;
	}

	public double getToLongtitude() {
		return toLongtitude;
	}

	public void setToLongtitude(double toLongtitude) {
		this.toLongtitude = toLongtitude;
	}

	/**
	 * 得到经度前一位
	 * 
	 * @return
	 */
	public static float getLatPrefix(double latitude) {
		return (float) (Math.round(latitude * 10)) / 10;
	}

	/**
	 * 得到纬度前一位
	 * 
	 * @return
	 */
	public static float getLngPrefix(double longtitude) {
		return (float) (Math.round(longtitude * 10)) / 10;
	}

	public void setRealLat(double latitude) {
		this.latitude = (float) (latitude + this.toLatitude);
	}

	public void setRealLng(double longtitude) {
		this.longtitude = (float) (longtitude + this.toLongtitude);
	}
}

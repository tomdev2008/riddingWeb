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
	private float latitude;
	/**
	 * 纬度
	 */
	private float longtitude;
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
	private float toLatitude;
	/**
	 * 偏移纬度
	 */
	private float toLongtitude;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(float longtitude) {
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

	public float getToLatitude() {
		return toLatitude;
	}

	public void setToLatitude(float toLatitude) {
		this.toLatitude = toLatitude;
	}

	public float getToLongtitude() {
		return toLongtitude;
	}

	public void setToLongtitude(float toLongtitude) {
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

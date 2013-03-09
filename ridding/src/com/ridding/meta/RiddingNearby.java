package com.ridding.meta;

import java.io.Serializable;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime 2013-2-8 23:15:13 Class Description 附近地图信息meta
 */
public class RiddingNearby implements Serializable {
	private static final long serialVersionUID = 80342774576L;
	/**
	 * 骑行Id
	 * 
	 */
	private long riddingId;
	/**
	 * 地图Id
	 */
	private long mapId;
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

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
	}

	public long getMapId() {
		return mapId;
	}

	public void setMapId(long mapId) {
		this.mapId = mapId;
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

}
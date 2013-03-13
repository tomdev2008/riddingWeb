package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-13 上午12:08:14
 * @see Class Description
 */
public class RiddingGps {

	private long id;
	private long userId;
	private long riddingId;
	private String mapPoint;
	private int distance;
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
	}

	public String getMapPoint() {
		return mapPoint;
	}

	public void setMapPoint(String mapPoint) {
		this.mapPoint = mapPoint;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}

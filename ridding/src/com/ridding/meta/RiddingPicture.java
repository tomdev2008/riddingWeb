package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-17 上午12:11:29 Class Description
 */
public class RiddingPicture {
	/**
	 * id
	 */
	private long id;
	/**
	 * 骑行id
	 */
	private long riddingId;
	/**
	 * 图片url
	 */
	private String photoUrl;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 最后更新时间
	 */
	private long lastUpdateTime;
	/**
	 * 照片状态
	 */
	private int status;
	/**
	 * 拍照的经度
	 */
	private double latitude;
	/**
	 * 拍照的纬度
	 */
	private double longtitude;
	/**
	 * 修正后的经度
	 */
	private double fixedLatitude;
	/**
	 * 修正后的纬度
	 */
	private double fixedLongtitude;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 拍照日期
	 */
	private long takePicDate;
	/**
	 * 拍照位置
	 */
	private String takePicLocation;
	/**
	 * 宽度
	 */
	private int width;
	/**
	 * 高度
	 */
	private int height;
	/**
	 * 对应userid的小头像
	 */
	private String sAvatorUrl;
	/**
	 * 是否已经喜欢过
	 */
	private boolean liked;
	/**
	 * 喜欢这张照片的数量
	 */
	private int likeCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
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

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public double getFixedLatitude() {
		return fixedLatitude;
	}

	public void setFixedLatitude(double fixedLatitude) {
		this.fixedLatitude = fixedLatitude;
	}

	public double getFixedLongtitude() {
		return fixedLongtitude;
	}

	public void setFixedLongtitude(double fixedLongtitude) {
		this.fixedLongtitude = fixedLongtitude;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getTakePicDate() {
		return takePicDate;
	}

	public void setTakePicDate(long takePicDate) {
		this.takePicDate = takePicDate;
	}

	public String getTakePicLocation() {
		return takePicLocation;
	}

	public void setTakePicLocation(String takePicLocation) {
		this.takePicLocation = takePicLocation;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getsAvatorUrl() {
		return sAvatorUrl;
	}

	public void setsAvatorUrl(String sAvatorUrl) {
		this.sAvatorUrl = sAvatorUrl;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

}

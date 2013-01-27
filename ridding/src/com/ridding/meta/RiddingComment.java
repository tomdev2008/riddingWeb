package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-8 下午11:33:31 Class Description
 */
public class RiddingComment {
	private long id;
	private long riddingId;
	private long userId;
	private long toUserId;
	private String text;
	private String usePicUrl;
	private int commentType;
	private long replyId;
	private long CreateTime;
	private long LastUpdateTime;

	private Profile toUserProfile;
	private Profile userProfile;

	private long lastCreateTime;
	private int limit;
	private boolean larger;

	public Profile getToUserProfile() {
		return toUserProfile;
	}

	public void setToUserProfile(Profile toUserProfile) {
		this.toUserProfile = toUserProfile;
	}

	public Profile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(Profile userProfile) {
		this.userProfile = userProfile;
	}

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

	public long getToUserId() {
		return toUserId;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsePicUrl() {
		return usePicUrl;
	}

	public void setUsePicUrl(String usePicUrl) {
		this.usePicUrl = usePicUrl;
	}

	public int getCommentType() {
		return commentType;
	}

	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}

	public long getReplyId() {
		return replyId;
	}

	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public long getLastUpdateTime() {
		return LastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		LastUpdateTime = lastUpdateTime;
	}

	public long getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(long lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isLarger() {
		return larger;
	}

	public void setLarger(boolean larger) {
		this.larger = larger;
	}
}

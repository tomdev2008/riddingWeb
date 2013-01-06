package com.ridding.meta.vo;

import com.ridding.meta.Profile;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-1-4 上午12:07:19 Class Description
 */
public class UserRelationVO {
	private long createTime;
	/**
 * 
 */
	private Profile userProfile;
	/**
	 * 
	 */
	private Profile toUserProfile;

	private int status;

	public Profile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(Profile userProfile) {
		this.userProfile = userProfile;
	}

	public Profile getToUserProfile() {
		return toUserProfile;
	}

	public void setToUserProfile(Profile toUserProfile) {
		this.toUserProfile = toUserProfile;
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

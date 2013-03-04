package com.ridding.meta.vo;

import java.util.List;

import com.ridding.meta.Profile;
import com.ridding.meta.UserPay;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-3 下午03:16:40 Class Description
 */
public class ProfileTaobaoVO {
	private Profile profile;
	private List<UserPay> userPays;

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<UserPay> getUserPays() {
		return userPays;
	}

	public void setUserPays(List<UserPay> userPays) {
		this.userPays = userPays;
	}

}

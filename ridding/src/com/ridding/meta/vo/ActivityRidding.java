package com.ridding.meta.vo;

import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.RiddingUser;

import com.ridding.meta.Ridding;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-9 下午08:47:33 Class Description
 */
public class ActivityRidding {

	private Ridding ridding;

	private RiddingUser riddingUser;

	private IMap iMap;

	private Profile leaderProfile;

	public Profile getLeaderProfile() {
		return leaderProfile;
	}

	public void setLeaderProfile(Profile leaderProfile) {
		this.leaderProfile = leaderProfile;
	}

	public Ridding getRidding() {
		return ridding;
	}

	public void setRidding(Ridding ridding) {
		this.ridding = ridding;
	}

	public IMap getiMap() {
		return iMap;
	}

	public void setiMap(IMap iMap) {
		this.iMap = iMap;
	}

	public RiddingUser getRiddingUser() {
		return riddingUser;
	}

	public void setRiddingUser(RiddingUser riddingUser) {
		this.riddingUser = riddingUser;
	}

}

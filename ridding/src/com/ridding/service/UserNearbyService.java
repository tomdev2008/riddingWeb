package com.ridding.service;

import java.util.List;

import com.ridding.meta.Profile;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime2013-1-29 00:16:03 Class Description
 */
public interface UserNearbyService {
	/**
	 * 添加或更新附近用户
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public int addOrUpdateUsersNearby(long userId, double latitude,
			double longitude);

	/**
	 * 获取附近用户列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Profile> showUserNearbyList(long userId);
}
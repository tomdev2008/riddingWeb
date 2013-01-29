package com.ridding.service;

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
	public boolean addOrUpdateUsersNearby(long userId, double latitude,
			double longitude);
}
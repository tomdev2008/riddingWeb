package com.ridding.mapper;

import java.util.Map;

import com.ridding.meta.UserNearby;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime 2013-1-29 13:50:39 Class Description
 */
public interface UserNearbyMapper {
	/**
	 * 添加附近用户
	 * 
	 * @param userNearby
	 * @return
	 */
	public boolean addUserNearby(UserNearby userNearby);

	/**
	 * 更新附近用户
	 * 
	 * @param userNearby
	 * @return
	 */
	public boolean updateUserNearby(UserNearby userNearby);

	/**
	 * 根据userId获取对象
	 * 
	 * @param userId
	 * @return
	 */
	public UserNearby getUserNearby(long userId);
}
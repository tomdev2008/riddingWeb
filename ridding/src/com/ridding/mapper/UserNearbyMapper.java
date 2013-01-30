package com.ridding.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	public int addUserNearby(UserNearby userNearby);

	/**
	 * 更新附近用户
	 * 
	 * @param userNearby
	 * @return
	 */
	public int updateUserNearby(UserNearby userNearby);

	/**
	 * 根据userId获取对象
	 * 
	 * @param userId
	 * @return
	 */
	public UserNearby getUserNearby(long userId);

	/**
	 * 通过geohash获取附近用户列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserNearby> getUserNearbyList(@Param(value="geohash")String geohash);
}
package com.ridding.service;

import java.util.List;

import com.ridding.meta.Profile;
import com.ridding.meta.RiddingNearby;

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
	public boolean addOrUpdateUsersNearby(long userId, double latitude, double longitude);

	/**
	 * 异步执行更新操作
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public boolean asyncUpdateUserNearBy(final long userId, final double latitude, final double longitude);

	/**
	 * 获取附近用户列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Profile> showUserNearbyList(long userId);

	/**
	 * 通过经纬度获得附近用户
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public List<Profile> showUserNearByList(double latitude, double longitude, int limit, int offset);

	/**
	 * 添加附近骑行活动定时任务
	 * 
	 * @auther zyslovely@gmail.com
	 */
	public void addRiddingNearbyQuartz();

	/**
	 * 附近的骑行活动
	 * 
	 * @auther zyslovely@gmail.com
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<RiddingNearby> showRddingNearByList(long userId, double latitude, double longitude, int limit, int offset);

}
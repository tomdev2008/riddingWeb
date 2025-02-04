package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.RiddingUser;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 12:50:48 Class Description
 */
public interface RiddingUserMapper {
	/**
	 * 添加骑行用户
	 * 
	 * @param riddingUser
	 * @return
	 */
	public int addRiddingUser(RiddingUser riddingUser);

	/**
	 * 得到骑行用户
	 * 
	 * @param hashMap
	 * @return
	 */
	public RiddingUser getRiddingUser(Map<String, Object> hashMap);

	/**
	 * 更新骑行名称
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updateRiddingName(Map<String, Object> hashMap);

	/**
	 * 更新骑行状态
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updateRiddingStatus(Map<String, Object> hashMap);

	/**
	 * 更新骑行用户状态
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updateRiddingUserRole(Map<String, Object> hashMap);

	/**
	 * 得到骑行用户列表，通过时间
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<RiddingUser> getSelfRiddingList(Map<String, Object> hashMap);

	/**
	 * 通过骑行id得到骑行列表
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<RiddingUser> getRiddingUserListByRiddingId(Map<String, Object> hashMap);

	/**
	 * 测试删除操作
	 * 
	 * @param hashMap
	 * @return
	 */
	public int deleteRiddingUser(Map<String, Object> hashMap);

	/**
	 * 得到骑行数量
	 * 
	 * @param userid
	 * @return
	 */
	public int getRiddingCount(long userId);

	/**
	 * 得到用户的骑行列表
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<RiddingUser> getRiddingListByUserId(Map<String, Object> hashMap);

	/**
	 * 根据riddingId删除
	 * 
	 * 
	 */
	public int deleteRiddingUserByRiddingId(long riddingId);

	/**
	 * 根据userId获得RiddingUser对象
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<RiddingUser> getRiddingUsersByUserId(Map<String, Object> hashMap);

	/**
	 * 更新同步状态
	 * 
	 * @auther zyslovely@gmail.com
	 * @param riddingId
	 * @param isOnlyWifi
	 * @return
	 */
	public int updateRiddingSyncWifi(@Param(value = "userId") long userId, @Param(value = "riddingId") long riddingId,
			@Param(value = "isSyncWifi") int isSyncWifi);

	/**
	 * 使用gps记录
	 * 
	 * @auther zyslovely@gmail.com
	 * @param userId
	 * @param riddingId
	 * @param isGps
	 * @return
	 */
	public int updateRiddingGps(@Param(value = "userId") long userId, @Param(value = "riddingId") long riddingId, @Param(value = "isGps") int isGps);
}

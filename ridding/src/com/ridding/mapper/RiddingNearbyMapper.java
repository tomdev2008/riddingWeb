package com.ridding.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.RiddingNearby;
import com.ridding.meta.UserNearby;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime 2013-2-8 23:13:39 Class Description
 */
public interface RiddingNearbyMapper {
	/**
	 * 添加附近地图
	 * 
	 * @param riddingNearby
	 * @return
	 */
	public int addRiddingNearby(RiddingNearby riddingNearby);

	/**
	 * 
	 * @auther zyslovely@gmail.com
	 * @param limit
	 * @return
	 */
	public List<RiddingNearby> getRiddingNearbyList(int limit);

	/**
	 * 通过geohash获取附近骑行活动列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<RiddingNearby> getRiddingNearbyListByGeo(@Param(value = "geohash") String geohash);

	/**
	 * 得到附近数量
	 * 
	 * @auther zyslovely@gmail.com
	 * @param geohash
	 * @param userId
	 * @return
	 */
	public int getRiddingNearbyCount(@Param(value = "geohash") String geohash);

	/**
	 * 删除
	 * 
	 * @auther zyslovely@gmail.com
	 * @param riddingId
	 * @return
	 */
	public int deleteRiddingNearBy(@Param(value = "riddingId") long riddingId);
}
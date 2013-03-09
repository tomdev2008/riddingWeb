package com.ridding.mapper;

import java.util.List;

import com.ridding.meta.RiddingNearby;

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

	public List<RiddingNearby> getRiddingNearbyList(int limit);
}
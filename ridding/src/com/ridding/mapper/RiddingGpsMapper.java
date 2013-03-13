package com.ridding.mapper;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.RiddingGps;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-13 上午12:07:24
 * @see Class Description
 */
public interface RiddingGpsMapper {

	/**
	 * 添加gps
	 * 
	 * @auther zyslovely@gmail.com
	 * @param riddingGps
	 * @return
	 */
	public int addRiddingGps(RiddingGps riddingGps);

	/**
	 * 更新gpspoint
	 * 
	 * @auther zyslovely@gmail.com
	 * @param riddingId
	 * @param userId
	 * @param mapPoint
	 * @return
	 */
	public int updateRiddingGpsPoint(@Param(value = "riddingId") long riddingId, @Param(value = "userId") long userId,
			@Param(value = "mapPoint") long mapPoint);
}

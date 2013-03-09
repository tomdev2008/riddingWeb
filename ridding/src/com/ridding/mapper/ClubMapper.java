package com.ridding.mapper;

import com.ridding.meta.Club;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:29:13 Class Description
 */
public interface ClubMapper {
	/**
	 * 添加俱乐部
	 * 
	 * @param club
	 * @return
	 */
	public int addClub(Club club);

	/**
	 * 通过俱乐部名称查找俱乐部
	 * 
	 * @param clubName
	 * @return
	 */
	public Club getClubByName(String clubName);

	/**
	 * 通过俱乐部Id超找俱乐部
	 * 
	 * @param clubId
	 * @return
	 */
	public Club getClubByClubId(long clubId);
}

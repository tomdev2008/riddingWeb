package com.ridding.service;

import com.ridding.meta.ClubMember;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:19:52 Class Description
 */
public interface ClubService {
	/**
	 * 申请创建俱乐部
	 * 
	 * @return
	 */
	public boolean applyForCreateClub(long userId, String clubName,
			String clubLocation, String clubDescription);

	/**
	 * 申请加入俱乐部成为会员
	 * 
	 * @param userId
	 * @param clubId
	 * @return
	 */
	public boolean applyForJoinClub(long userId, long clubId);

	/**
	 * 同意或拒绝俱乐部申请
	 * 
	 * @return
	 */
	public boolean agreeOrRefuseToCreateClub(long applyId, int status);

	/**
	 * 同意或拒绝加入俱乐部申请
	 * 
	 * @param applyId
	 * @param status
	 * @return
	 */
	public boolean agreeOrRefuseToJoinClub(long applyId, int status);

	/**
	 * 添加俱乐部成员
	 * 
	 * @param userId
	 * @param clubId
	 * @return
	 */
	public boolean addClubMember(long userId, long clubId);

	/**
	 * 删除俱乐部成员
	 * 
	 * @param userId
	 * @param clubId
	 * @return
	 */
	public boolean removeClubMember(long userId, long clubId);

	/**
	 * 获取俱乐部成员
	 * 
	 * @param userId
	 * @param clubId
	 * @return
	 */
	public ClubMember getClubMember(long userId, long clubId);
}
package com.ridding.mapper;

import java.util.Map;

import com.ridding.meta.ClubMember;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:42:55 Class Description
 */
public interface ClubMemberMapper {
	/**
	 * 添加俱乐部会员
	 * 
	 * @param clubMember
	 * @return
	 */
	public int addClubMember(ClubMember clubMember);

	/**
	 * 获取俱乐部会员根据userId,clubId
	 * 
	 * @param map
	 * @return
	 */
	public ClubMember getClubMember(Map<String, Object> map);

	/**
	 * 更新俱乐部会员
	 * 
	 * @param clubMember
	 * @return
	 */
	public int updateClubMember(ClubMember clubMember);
}

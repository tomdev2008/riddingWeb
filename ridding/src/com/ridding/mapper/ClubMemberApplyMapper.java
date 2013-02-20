package com.ridding.mapper;

import java.util.HashMap;
import java.util.Map;

import com.ridding.meta.ClubMemberApply;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-19 上午12:02:59 Class Description
 */
public interface ClubMemberApplyMapper {
	/**
	 * 添加俱乐部会员申请
	 * 
	 * @param clubMemberApply
	 * @return
	 */
	public int addClubMemberApply(ClubMemberApply clubMemberApply);

	/**
	 * 更新俱乐部会员申请状态
	 * 
	 * @param clubMemberApply
	 * @return
	 */
	public int updateClubMemberApply(ClubMemberApply clubMemberApply);

	/**
	 * 获取俱乐部会员申请单
	 * 
	 * @param id
	 * @return
	 */
	public ClubMemberApply getClubMemberApply(long id);

	/**
	 * 获取俱乐部会员申请单通过userId,clubId
	 * 
	 * @param userId
	 * @param clubId
	 * @return
	 */
	public ClubMemberApply getClubMemberApplyByUserIdAndClubId(
			Map<String, Object> hashMap);
}

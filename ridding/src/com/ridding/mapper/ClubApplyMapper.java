package com.ridding.mapper;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.ClubApply;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:52:05 Class Description
 */
public interface ClubApplyMapper {
	/**
	 * 添加俱乐部申请
	 * 
	 * @param clubApply
	 * @return
	 */
	public int addClubApply(ClubApply clubApply);

	/**
	 * 更新俱乐部申请状态
	 * 
	 * @param id
	 * @return
	 */
	public int updateClubApply(ClubApply clubApply);

	/**
	 * 获取俱乐部申请单通过id
	 * 
	 * @param id
	 * @return
	 */
	public ClubApply getClubApply(long id);

	/**
	 * 获取俱乐部申请单通过clubName
	 * 
	 * @param map
	 * @return
	 */
	public ClubApply getcClubApplyByClubName(
			@Param(value = "clubName") String clubName);
}

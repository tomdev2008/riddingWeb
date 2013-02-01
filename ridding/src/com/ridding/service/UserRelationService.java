package com.ridding.service;

import java.util.List;

import com.ridding.meta.UserRelation;
import com.ridding.meta.RiddingAction.RiddingActionResponse;
import com.ridding.meta.vo.UserRelationVO;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-1-3 下午11:43:13 Class Description
 */
public interface UserRelationService {
	/**
	 * 添加用户关系
	 * 
	 * @param userId
	 * @param toUserId
	 * @return
	 */
	public RiddingActionResponse addUserRelation(long userId, long toUserId);

	/**
	 * 得到某个用户的用户关系
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRelationVO> getUserRelations(long userId);

	/**
	 * 删除用户关系
	 * 
	 * @param userId
	 * @param toUserId
	 * @return
	 */
	public int deleteUserRelation(long userId, long toUserId);

	/**
	 * 更新用户关系
	 * 
	 * @param userRelation
	 * @return
	 */
	public boolean updateUserRelation(UserRelation userRelation);

	/**
	 * 添加或删除用户关系
	 * 
	 * @param userId
	 * @param toUserId
	 * @param status
	 * @return
	 */
	public RiddingActionResponse removeOrAddUserRelation(long userId,
			long toUserId, int status);
}

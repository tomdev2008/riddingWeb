package com.ridding.service;

import java.util.List;

import com.ridding.constant.RiddingQuitConstant;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingAction;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.RiddingAction.RiddingActionResponse;
import com.ridding.meta.RiddingAction.RiddingActions;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.meta.vo.ProfileVO;

/**
 * @author zhengyisheng E-mail:zhengyisheng@corp.netease.com
 * @version CreateTime2012-3-5 11:56:03 Class Description
 */
public interface RiddingService {
	/**
	 * 得到所有骑行用户列表
	 * 
	 * @param riddingId
	 * @param riddingUser
	 * @return
	 */
	public List<RiddingUser> getAllRiddingUserList(RiddingUser riddingUser);

	/**
	 * 得到骑行用户
	 * 
	 * @param riddingId
	 * @param userid
	 * @return
	 */
	public RiddingUser getRiddingUser(long riddingId, long userId);

	/**
	 * 更新骑行名称
	 * 
	 * @param ridding
	 * @param userId
	 * @param name
	 * @return
	 */
	public boolean updateRiddingName(long ridingId, long userId, String name);

	/**
	 * 结束骑行
	 * 
	 * @param ridingId
	 * @param userId
	 * @return
	 */
	public boolean endRidding(long ridingId, long userId);

	/**
	 * 队长结束骑行活动
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public boolean endRiddingByLeader(long riddingId, long userId);

	/**
	 * 根据id得到骑行对象
	 * 
	 * @param id
	 * @return
	 */
	public Ridding getRidding(long id);

	/**
	 * 得到骑行列表
	 * 
	 * @param id
	 * @param limit
	 * @param createTime
	 * @return
	 */
	public List<ActivityRidding> getSelfRiddingUserList(long userId, int limit,
			long createTime, boolean isLarger);

	/**
	 * 通过骑行id得到所有用户信息
	 * 
	 * @param riddingId
	 * @param limit
	 * @param createTime
	 * @return
	 */
	public List<ProfileVO> getRiddingUserListToProfile(long riddingId,
			int limit, int createTime);

	/**
	 * 添加骑行用户
	 * 
	 * @param sinaIdList
	 * @return
	 */
	public boolean insertRiddingUsers(List<Profile> profileList,
			long riddingId, int sourceType, long userId);

	/**
	 * 删除骑行用户
	 * 
	 * @param sinaIdList
	 * @param riddingId
	 * @return
	 */
	public boolean deleteRiddingUsers(List<Long> sinaUseridList, long riddingId);

	/**
	 * 退出骑行
	 * 
	 * @param userId
	 * @param riddingId
	 * @return
	 */
	public RiddingQuitConstant quitRidding(long userId, long riddingId);

	/**
	 * 得到用户的骑行活动数量
	 * 
	 * @param userId
	 * @return
	 */
	public int getRiddingCount(long userId);

	/**
	 * 得到骑行列表通过limit ,offset
	 * 
	 * @param userId
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<ActivityRidding> getRiddingListbyUserId(long userId, int limit,
			int offset);

	/**
	 * 添加骑行活动
	 * 
	 * @param ridding
	 * @return
	 */
	public Ridding addRidding(Ridding ridding);

	/**
	 * 添加骑行图片
	 * 
	 * @param riddingPicture
	 * @return
	 */
	public int addRiddingPicture(RiddingPicture riddingPicture);

	/**
	 * 得到骑行图片
	 * 
	 * @param riddingId
	 * @param userid
	 * @return
	 */
	public List<RiddingPicture> getRiddingPictureByRiddingId(long riddingId,
			int limit, long lastUpdateTime);

	/**
	 * 根据最近更新的骑行活动
	 * 
	 * @param lastUpdateTime
	 * @param limit
	 * @return
	 */
	public List<Ridding> getRiddingListByLastUpdateTime(long lastUpdateTime,
			int limit, Boolean isLarger, int isRecom);

	/**
	 * 得到推荐的骑行活动
	 * 
	 * @param weight
	 * @param limit
	 * @param isLarger
	 * @return
	 */
	public List<Ridding> getRecomRiddingList(int weight, int limit,
			Boolean isLarger);

	/**
	 * 设置骑行活动为推荐
	 * 
	 */
	public boolean setRiddingIsRecom(long riddingId);

	/**
	 * 增加喜欢
	 * 
	 * @param riddingId
	 * @return
	 */
	public RiddingActionResponse incRiddingLike(long riddingId, long userId);

	/**
	 * 增加收藏
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public RiddingActionResponse incRiddingUse(long riddingId, long userId);

	/**
	 * 增加关注
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public RiddingActionResponse incRiddingCare(long riddingId, long userId);

	/**
	 * 增加关注
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public boolean incRiddingComment(long riddingId);

	/**
	 * 检查是否已经操作过
	 * 
	 * @param riddingId
	 * @param userId
	 * @param action
	 * @return
	 */
	public boolean checkIsInRiddingAction(long riddingId, long userId,
			RiddingActions action);

	/**
	 * 检查用户是否在骑行活动中
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public boolean checkIsInRidding(long riddingId, long userId);

	/**
	 * 得到骑行图片列表
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public List<RiddingPicture> getRiddingPictureList(long riddingId,
			long userId, int limit, long createTime);

	/**
	 * 得到用户操作记录,判断用户是否喜欢过，使用过，关注过
	 * 
	 * @param userId
	 * @param riddingId
	 * @return
	 */
	public RiddingAction getUserAction(long userId, long riddingId);
}

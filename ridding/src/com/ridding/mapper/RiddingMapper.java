package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.Ridding;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 12:50:39 Class Description
 */
public interface RiddingMapper {
	/**
	 * 添加骑行活动
	 * 
	 * @param ridding
	 * @return
	 */
	public int addRidding(Ridding ridding);

	/**
	 * 得到骑行活动
	 * 
	 * @param riddingId
	 * @return
	 */
	public Ridding getRidding(long riddingId);

	/**
	 * 增加用户数
	 * 
	 * @param hashMap
	 * @return
	 */
	public int increaseUserCount(Map<String, Object> hashMap);

	/**
	 * 得到骑行列表,已删除的不算
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<Ridding> getRiddingList(List<Long> ids);

	/**
	 * 删除骑行
	 * 
	 * @param riddingId
	 * @return
	 */
	public int deleteRidding(long riddingId);

	/**
	 * 更新骑行状态
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updateRiddingStatus(Map<String, Object> hashMap);

	/**
	 * 更新公开状态
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updatePublicStatus(Map<String, Object> hashMap);

	/**
	 * 更新推荐状态
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updateRecomStatus(Map<String, Object> hashMap);

	/**
	 * 通过更新时间得到骑行列表
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<Ridding> getRiddingListByLastUpdateTime(Map<String, Object> hashMap);

	/**
	 * 喜欢数
	 * 
	 * @param riddingId
	 * @return
	 */
	public int incLikeCount(long riddingId);

	/**
	 * 喜欢数
	 * 
	 * @param riddingId
	 * @return
	 */
	public int incCareCount(long riddingId);

	/**
	 * 喜欢数
	 * 
	 * @param riddingId
	 * @return
	 */
	public int incUseCount(long riddingId);

	/**
	 * 评论数
	 * 
	 * @param riddingId
	 * @return
	 */
	public int incCommentCount(long riddingId);

	/**
	 * 照片数增加
	 * 
	 * @param riddingId
	 * @return
	 */
	public int incPictureCount(long riddingId);

	/**
	 * 照片数减少
	 * 
	 * @param riddingId
	 * @return
	 */
	public int decPictureCount(long riddingId);

	/**
	 * 取出喜欢数排行骑行
	 * 
	 * @param map
	 * @return
	 */
	public List<Ridding> getRiddingsbyLike(Map<String, Object> map);

	/**
	 * 取出评论数排行骑行
	 * 
	 * @param map
	 * @return
	 */
	public List<Ridding> getRiddingsbyComment(Map<String, Object> map);

	/**
	 * 取出使用数排行骑行
	 * 
	 * @param map
	 * @return
	 */
	public List<Ridding> getRiddingsbyUse(Map<String, Object> map);

	/**
	 * 获取所有骑行
	 * 
	 * @return
	 */
	public List<Ridding> getAllRidding();

	/**
	 * 获取限定骑行Id后的骑行
	 * 
	 * @param riddingId
	 * @return
	 */
	public List<Ridding> getRiddingListByStartRiddingId(long riddingId);

	/**
	 * 
	 * 取出照片数排行骑行
	 * 
	 * @param map
	 * @return
	 */
	public List<Ridding> getRiddingsbyPicture(Map<String, Object> map);

	/**
	 * 更新照片数量
	 * 
	 * @param riddingId
	 * @param pictureCount
	 * @return
	 */
	public int updateRiddingPictureCount(@Param(value = "riddingId") long riddingId, @Param(value = "pictureCount") int pictureCount);
}

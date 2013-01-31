package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.RiddingAction;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-8 下午06:40:49 Class Description
 */
public interface RiddingActionMapper {
	/**
	 * 添加
	 * 
	 * @param riddingLike
	 * @return
	 */
	public int addRiddingAction(RiddingAction riddingLike);

	/**
	 * 得到操作通过userid，riddingId
	 * 
	 * @param map
	 * @return
	 */
	public RiddingAction getRiddingActionByUserId(Map<String, Object> map);

	/**
	 * 得到用户的操作记录
	 * 
	 * @param map
	 * @return
	 */
	public List<RiddingAction> getRiddingActionsByUserId(Map<String, Object> map);

	/**
	 * 通过riddingId删除操作
	 * 
	 * 
	 */
	public int deleteRiddingActionByRiddingId(long riddingId);

	/**
	 * 通过objectId添加
	 * 
	 * @param riddingLike
	 * @return
	 */
	public int addRiddingActionByObjectId(RiddingAction riddingAction);
}

package com.ridding.service.transaction;

import net.sf.json.JSONObject;

import org.springframework.transaction.TransactionException;

import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.Source;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.WeiBo;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-21 12:22:49 Class Description
 */
public interface TransactionService {
	/**
	 * 插入用户信息
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public Profile insertSourceAccount(SourceAccount sourceAccount,
			Profile profile) throws TransactionException;

	/**
	 * 插入地图
	 * 
	 * @param iMap
	 * @param sinaUserId
	 * @return
	 * @throws Exception
	 */
	public boolean insertMap(IMap iMap, Source sinaWeiBo)
			throws TransactionException;

	/**
	 * 插入骑行用户
	 * 
	 * @param riddingId
	 * @param userList
	 * @return
	 */
	public boolean insertRiddingUser(Ridding ridding, Profile profile,
			int sourceType, Profile leaderProfile) throws TransactionException;

	/**
	 * 插入一个新的骑行纪录
	 * 
	 * @param iMap
	 * @return
	 */
	public boolean insertANewRidding(IMap iMap, Ridding ridding);

	/**
	 * 队长结束骑行活动
	 * 
	 * @param riddingId
	 * @return
	 */
	public boolean updateEndRiddingByLeader(long riddingId, int distance)
			throws TransactionException;

	/**
	 * 对新转发的微博，并创建骑行活动
	 * 
	 * @param response
	 * @param weiBo
	 */
	public long insertRepostMap(WeiBo weiBo, JSONObject jsonObject2)
			throws TransactionException;

	/**
	 * 删除骑行队员、图片、评论、操作
	 * 
	 * @param riddingId
	 * @return
	 */
	public boolean deleteRiddingAndLinkedThings(long riddingId)
			throws TransactionException;


}

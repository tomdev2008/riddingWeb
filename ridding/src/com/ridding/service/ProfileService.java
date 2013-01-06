package com.ridding.service;

import java.util.List;

import com.ridding.meta.Profile;
import com.ridding.meta.SourceAccount;

/**
 * @author zhengyisheng E-mail:zhengyisheng@corp.netease.com
 * @version CreateTime 2012-3-5 12:05:48 Class Description
 */
public interface ProfileService {
	/**
	 * 获取帐户信息
	 * 
	 * @param account
	 * @return
	 */
	public SourceAccount addAccount(SourceAccount account);

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Profile getProfile(long userId);

	/**
	 * 增加用户骑行距离
	 * 
	 * @param userId
	 * @param distance
	 * @return
	 */
	public boolean incUserTotalDistance(long userId, int distance);

	/**
	 * 得到用户信息列表
	 * 
	 * @param userids
	 * @return
	 */
	public List<Profile> getProfileList(List<Long> userids);

	/**
	 * 通过新浪微博id的到帐户信息
	 * 
	 * @param userids
	 * @return
	 */
	public List<SourceAccount> getAccountBySourceUserIds(List<Long> sourceUserids, int sourceType);

	/**
	 * 通过外域资源用户id得到帐户
	 * 
	 * @param sinaUserId
	 * @return
	 */
	public SourceAccount getSourceAccountByAccessUserId(long accessUserId, int sourceType);

	/**
	 * 更新用户信息
	 * 
	 * @param bavatorUrl
	 * @param savatorUrl
	 * @param userName
	 * @return
	 */
	public Profile updateProfile(String bavatorUrl, String savatorUrl, String userName, long userId);

	/**
	 * 通过userIds和soureceType得到对应的列表
	 * 
	 * @param userIds
	 * @param sourceType
	 * @return
	 */
	public List<SourceAccount> getSourceAccountByUserIdsSourceType(List<Long> userIds, int sourceType);

	/**
	 * 得到SourceAccount
	 * 
	 * @param userId
	 * @param sourceType
	 * @return
	 */
	public SourceAccount getSourceAccountByUserIdsSourceType(Long userId, int sourceType);

	/**
	 * 更新背景url
	 * 
	 * @param url
	 * @param userId
	 * @return
	 */
	public boolean updateBackgroundUrl(String url, long userId);
}

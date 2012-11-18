package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.SourceAccount;

/**
 * @author zhengyisheng E-mail:zhengyisheng@corp.netease.com
 * @version CreateTime 2012-3-5 11:45:50 Class Description 帐户信息mapper
 */
public interface SourceAccountMapper {
	/**
	 * 添加帐户信息
	 * 
	 * @param account
	 * @return
	 */
	public int addSourceAccount(SourceAccount sourceAccount);

	/**
	 * 通过外域资源id和类型得到帐户 long accessUserId, long sourceType
	 * 
	 * @param sinaUserId
	 * @return
	 */
	public SourceAccount getSourceAccountByAccessUserId(Map<String, Object> hashMap);

	/**
	 * 更新用户信息
	 * 
	 * @param account
	 * @return
	 */
	public int updateSourceAccount(SourceAccount sourceAccount);

	/**
	 * 通过新浪微博id得到帐户,list<accessUserId> sourceType
	 * 
	 * @param sinaWeiBoUserids
	 * @return
	 */
	public List<SourceAccount> getSourceAccountsByAccessUserIds(Map<String, Object> hashMap);

	/**
	 * 通过userIds,sourceType得到list
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<SourceAccount> getSourceAccountByUserIdsSourceType(Map<String, Object> hashMap);

}

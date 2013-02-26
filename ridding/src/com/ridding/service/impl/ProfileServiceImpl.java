package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.constant.SystemConst;
import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.SourceAccountMapper;
import com.ridding.meta.Profile;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.UserNearby;
import com.ridding.service.ProfileService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.GeohashUtil;
import com.ridding.util.ListUtils;
import com.ridding.util.QiNiuUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@corp.netease.com
 * @version CreateTime 2012-3-5 12:06:03 Class Description 用户信息service
 */
@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

	@Resource
	private SourceAccountMapper sourceAccountMapper;
	@Resource
	private ProfileMapper profileMapper;

	@Resource
	private TransactionService transactionService;

	private static final Logger logger = Logger.getLogger(ProfileServiceImpl.class);



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ProfileService#addAccount(com.ridding.meta.Account)
	 */
	public SourceAccount addAccount(SourceAccount sourceAccount) {
		if (sourceAccount == null) {
			return null;
		}
		try {
			transactionService.insertSourceAccount(sourceAccount, null);
			return sourceAccount;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ProfileService#getProfile(long)
	 */
	public Profile getProfile(long userId) {
		return profileMapper.getProfile(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ProfileService#incUserTotalDistance(long, int)
	 */
	public boolean incUserTotalDistance(long userId, int distance) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("totalDistance", distance);
		return profileMapper.incUserTotalDistance(hashMap) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ProfileService#getProfileList(java.util.List)
	 */
	public List<Profile> getProfileList(List<Long> userids) {
		if (ListUtils.isEmptyList(userids)) {
			return null;
		}
		return profileMapper.getProfileList(userids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ProfileService#getAccountBySourceUserIds(java.util
	 * .List, int)
	 */
	public List<SourceAccount> getAccountBySourceUserIds(List<Long> sourceUserids, int sourceType) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("sourceType", sourceType);
		hashMap.put("sourceUserids", sourceUserids);
		return sourceAccountMapper.getSourceAccountsByAccessUserIds(hashMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ProfileService#getSourceAccountByAccessUserId(long,
	 * int)
	 */
	public SourceAccount getSourceAccountByAccessUserId(long accessUserId, int sourceType) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("sourceType", sourceType);
		hashMap.put("accessUserId", accessUserId);
		return sourceAccountMapper.getSourceAccountByAccessUserId(hashMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ProfileService#updateProfile(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Profile updateProfile(String bavatorUrl, String savatorUrl, String userName, long userId) {
		Profile profile = profileMapper.getProfile(userId);
		if (profile == null) {
			return null;
		}
		profile.setbAvatorUrl(bavatorUrl);
		profile.setsAvatorUrl(savatorUrl);
		profile.setUserName(userName);
		profile.setNickName(userName);
		
		if (profileMapper.updateProfile(profile) > 0) {
			return profile;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ProfileService#getSourceAccountByUserIdsSourceType
	 * (java.util.List, int)
	 */
	@Override
	public List<SourceAccount> getSourceAccountByUserIdsSourceType(List<Long> userIds, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userIds", userIds);
		map.put("sourceType", sourceType);
		return sourceAccountMapper.getSourceAccountByUserIdsSourceType(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ProfileService#getSourceAccountByUserIdsSourceType
	 * (java.lang.Long, int)
	 */
	@Override
	public SourceAccount getSourceAccountByUserIdsSourceType(Long userId, int sourceType) {
		List<Long> list = new ArrayList<Long>();
		list.add(userId);
		List<SourceAccount> sourceAccounts = this.getSourceAccountByUserIdsSourceType(list, sourceType);
		if (ListUtils.isEmptyList(sourceAccounts)) {
			return null;
		}
		return sourceAccounts.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ProfileService#updateBackgroundUrl(java.lang.String,
	 * long)
	 */
	@Override
	public boolean updateBackgroundUrl(String url, long userId) {
		if (url == null) {
			return false;
		}
		Profile profile = new Profile();
		profile.setUserId(userId);
		profile.setBackgroundUrl(url);
		return profileMapper.updateBackgroundUrl(profile) > 0;
	}
}

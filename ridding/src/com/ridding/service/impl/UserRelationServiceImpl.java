package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.constant.returnCodeConstance;
import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.UserRelationMapper;
import com.ridding.meta.Profile;
import com.ridding.meta.RiddingAction.RiddingActionResponse;
import com.ridding.meta.UserRelation;
import com.ridding.meta.vo.UserRelationVO;
import com.ridding.service.UserRelationService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;
import com.sun.mail.imap.protocol.Status;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-1-3 下午11:43:33 Class Description
 */
@Service("userRelationService")
public class UserRelationServiceImpl implements UserRelationService {

	@Resource
	private UserRelationMapper userRelationMapper;

	@Resource
	private ProfileMapper profileMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserRelationService#addUserRelation(long, long)
	 */
	@Override
	public RiddingActionResponse addUserRelation(long userId, long toUserId) {
		if (userId < 0 || toUserId < 0) {
			return RiddingActionResponse.Fail;
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("toUserId", toUserId);
		if (userRelationMapper.getUserRelation(hashMap) != null) {
			return RiddingActionResponse.Fail;
		} else {
			int status = 0;
			UserRelation userRelation = new UserRelation();
			userRelation.setUserId(userId);
			userRelation.setToUserId(toUserId);
			userRelation.setStatus(status);
			userRelation.setCreateTime(new Date().getTime());
			if (userRelationMapper.addUserRelation(userRelation) > 0) {
				return RiddingActionResponse.SUCC;
			}
			return RiddingActionResponse.Fail;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserRelationService#getUserRelations(long)
	 */
	public List<UserRelationVO> getUserRelations(long userId) {
		if (userId <= 0) {
			return null;
		}
		List<UserRelation> userRelationList = userRelationMapper
				.getUserRelations(userId);
		return this.getUserRelationVO(userRelationList);
	}

	/**
	 * 转换
	 * 
	 * @param userRelationList
	 * @return
	 */
	private List<UserRelationVO> getUserRelationVO(
			List<UserRelation> userRelationList) {
		if (!ListUtils.isEmptyList(userRelationList)) {
			List<Long> userIdList = new ArrayList<Long>();
			for (UserRelation userRelation : userRelationList) {
				userIdList.add(userRelation.getUserId());
				userIdList.add(userRelation.getToUserId());
			}
			List<Profile> profileList = profileMapper
					.getProfileList(userIdList);
			Map<Long, Profile> profileMap = HashMapMaker.listToMap(profileList,
					"getUserId", Profile.class);
			List<UserRelationVO> userRelationVOs = new ArrayList<UserRelationVO>(
					userRelationList.size());
			for (UserRelation userRelation : userRelationList) {
				UserRelationVO userRelationVO = new UserRelationVO();
				Profile userProfile = profileMap.get(userRelation.getUserId());
				if (userProfile != null) {
					userRelationVO.setUserProfile(userProfile);
				}
				Profile toUserProfile = profileMap
						.get(userRelation.getUserId());
				if (toUserProfile != null) {
					userRelationVO.setToUserProfile(toUserProfile);
				}
				userRelationVO.setCreateTime(userRelation.getCreateTime());
				userRelationVO.setStatus(userRelation.getStatus());
				userRelationVOs.add(userRelationVO);
			}
			return userRelationVOs;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserRelationService#removeUserRelation(long,
	 * long)
	 */
	public int deleteUserRelation(long userId, long toUserId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("toUserId", toUserId);
		return userRelationMapper.deleteUserRelation(hashMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.UserRelationService#updateUserRelation(com.ridding
	 * .meta.UserRelation)
	 */
	@Override
	public boolean updateUserRelation(UserRelation userRelation) {
		if (userRelation == null) {
			return false;
		}
		return userRelationMapper.updateUserRelation(userRelation) > 0;
	}

	public RiddingActionResponse removeOrAddUserRelation(long userId,
			long toUserId, int status) {
		if (status == 0) {
			int delete1 = 0, delete2 = 0;
			Map<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("userId", userId);
			hashMap.put("toUserId", toUserId);
			delete1 = userRelationMapper.deleteUserRelation(hashMap);
			hashMap.put("userId", toUserId);
			hashMap.put("toUserId", userId);
			delete2 = userRelationMapper.deleteUserRelation(hashMap);
			if (delete1 > 0 || delete2 > 0) {
				return RiddingActionResponse.SUCC;
			}
			return RiddingActionResponse.Fail;
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", toUserId);
		hashMap.put("toUserId", userId);
		if (userRelationMapper.getUserRelation(hashMap) == null) {
			return RiddingActionResponse.Fail;
		}
		UserRelation userRelation = new UserRelation();
		userRelation.setUserId(toUserId);
		userRelation.setToUserId(userId);
		userRelation.setCreateTime(new Date().getTime());
		userRelation.setStatus(status);
		int update = userRelationMapper.updateUserRelation(userRelation);
		userRelation.setUserId(userId);
		userRelation.setToUserId(toUserId);
		hashMap.put("userId", userId);
		hashMap.put("toUserId", toUserId);
		int add = 0;
		if (userRelationMapper.getUserRelation(hashMap) == null) {
			add = userRelationMapper.addUserRelation(userRelation);
		} else {
			update = userRelationMapper.updateUserRelation(userRelation);
		}
		if (add > 0 && update > 0) {
			return RiddingActionResponse.SUCC;
		}
		return RiddingActionResponse.Fail;
	}
}

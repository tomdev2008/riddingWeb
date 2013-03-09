package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.UserRelationMapper;
import com.ridding.meta.Profile;
import com.ridding.meta.UserRelation;
import com.ridding.meta.vo.UserRelationVO;
import com.ridding.service.UserRelationService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;

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
	public UserRelation addUserRelation(long userId, long toUserId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("toUserId", toUserId);
		UserRelation relation = userRelationMapper.getUserRelation(map);
		UserRelation userRelation = new UserRelation();
		userRelation.setUserId(userId);
		userRelation.setToUserId(toUserId);
		userRelation.setCreateTime(new Date().getTime());
		userRelation.setStatus(UserRelation.Valid);
		if (relation != null) {
			if (userRelationMapper.updateUserRelation(userRelation) > 0) {
				return userRelation;
			}
		} else {
			if (userRelationMapper.addUserRelation(userRelation) > 0) {
				return userRelation;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserRelationService#getUserRelations(long)
	 */
	@Override
	public List<UserRelationVO> getUserRelations(long userId) {
		if (userId <= 0) {
			return null;
		}
		List<UserRelation> userRelationList = userRelationMapper.getUserRelations(userId);
		return this.getUserRelationVO(userRelationList);
	}

	/**
	 * 转换
	 * 
	 * @param userRelationList
	 * @return
	 */
	private List<UserRelationVO> getUserRelationVO(List<UserRelation> userRelationList) {
		if (!ListUtils.isEmptyList(userRelationList)) {
			List<Long> userIdList = new ArrayList<Long>();
			for (UserRelation userRelation : userRelationList) {
				userIdList.add(userRelation.getUserId());
				userIdList.add(userRelation.getToUserId());
			}
			List<Profile> profileList = profileMapper.getProfileList(userIdList);
			Map<Long, Profile> profileMap = HashMapMaker.listToMap(profileList, "getUserId", Profile.class);
			List<UserRelationVO> userRelationVOs = new ArrayList<UserRelationVO>(userRelationList.size());
			for (UserRelation userRelation : userRelationList) {
				UserRelationVO userRelationVO = new UserRelationVO();
				Profile userProfile = profileMap.get(userRelation.getUserId());
				if (userProfile != null) {
					userRelationVO.setUserProfile(userProfile);
				}
				Profile toUserProfile = profileMap.get(userRelation.getUserId());
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
	@Override
	public boolean removeUserRelation(long userId, long toUserId) {
		UserRelation userRelation = new UserRelation();
		userRelation.setUserId(userId);
		userRelation.setToUserId(toUserId);
		userRelation.setStatus(UserRelation.notValid);
		return userRelationMapper.updateUserRelation(userRelation) > 0;
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

}

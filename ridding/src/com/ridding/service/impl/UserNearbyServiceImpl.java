package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.UserNearbyMapper;
import com.ridding.meta.Profile;
import com.ridding.meta.UserNearby;
import com.ridding.service.UserNearbyService;
import com.ridding.util.GeohashUtil;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime2013-1-29 00:16:03 Class Description
 */
@Service("userNearbyService")
public class UserNearbyServiceImpl implements UserNearbyService {
	@Resource
	private UserNearbyMapper userNearbyMapper;

	@Resource
	private ProfileMapper profileMapper;

	/**
	 * 添加或更新附近用户
	 * 
	 */
	public int addOrUpdateUsersNearby(long userId, double latitude,
			double longitude) {
		UserNearby userNearby = new UserNearby();
		userNearby.setUserId(userId);
		userNearby.setLatitude(latitude);
		userNearby.setLongitude(longitude);
		userNearby.setGeohash(GeohashUtil.encode(latitude, longitude));
		userNearby.setLastUpdateTime(new Date().getTime());
		if (userNearbyMapper.getUserNearby(userId) ==null ) {
			return userNearbyMapper.addUserNearby(userNearby);
		} else {
			return userNearbyMapper.updateUserNearby(userNearby);
		}
	}

	/**
	 * 显示附近用户
	 * 
	 */
	public List<Profile> showUserNearbyList(long userId) {
		UserNearby userNearby = userNearbyMapper.getUserNearby(userId);
		if (userNearby == null) {
			return null;
		}
		int offset = 0, endIndex = 2;
		String geohash = userNearby.getGeohash().substring(offset, endIndex);
		List<UserNearby> userNearbyList = userNearbyMapper
				.getUserNearbyList(geohash);
		List<Profile> userNearbyProfiles = new ArrayList<Profile>(
				userNearbyList.size());
		for (UserNearby userNearbies : userNearbyList) {
			Profile profile = profileMapper
					.getProfile(userNearbies.getUserId());
			userNearbyProfiles.add(profile);
		}
		return userNearbyProfiles;

	}
}
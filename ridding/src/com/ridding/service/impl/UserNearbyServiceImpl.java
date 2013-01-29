package com.ridding.service.impl;

import java.util.Date;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.UserNearbyMapper;
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

	/**
	 * 添加附近用户
	 * 
	 */
	public boolean addOrUpdateUsersNearby(long userId, double latitude,
			double longitude) {
		UserNearby userNearby = new UserNearby();
		userNearby.setUserId(userId);
		userNearby.setLatitude(latitude);
		userNearby.setLongitude(longitude);
		userNearby.setGeohash(GeohashUtil.encode(latitude, longitude));
		userNearby.setLastUpdateTime(new Date().getTime());
		if (userNearbyMapper.getUserNearby(userId) == null) {
			return userNearbyMapper.addUserNearby(userNearby);
		} else {
			return userNearbyMapper.addUserNearby(userNearby);
		}
	}
}
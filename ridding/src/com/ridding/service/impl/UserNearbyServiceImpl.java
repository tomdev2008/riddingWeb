package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.mapper.IMapMapper;
import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.RiddingMapper;
import com.ridding.mapper.RiddingNearbyMapper;
import com.ridding.mapper.UserNearbyMapper;
import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingNearby;
import com.ridding.meta.UserNearby;
import com.ridding.service.UserNearbyService;
import com.ridding.util.GeohashUtil;
import com.ridding.util.ListUtils;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime2013-1-29 00:16:03 Class Description
 */
@Service("userNearbyService")
public class UserNearbyServiceImpl implements UserNearbyService {

	private static final Logger logger = Logger.getLogger(UserNearbyServiceImpl.class);
	@Resource
	private UserNearbyMapper userNearbyMapper;

	@Resource
	private ProfileMapper profileMapper;

	@Resource
	private RiddingMapper riddingMapper;

	@Resource
	private IMapMapper iMapMapper;

	@Resource
	private RiddingNearbyMapper riddingNearbyMapper;

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 添加或更新附近用户
	 * 
	 */
	@Override
	public boolean addOrUpdateUsersNearby(long userId, double latitude, double longitude) {
		UserNearby userNearby = new UserNearby();
		userNearby.setUserId(userId);
		userNearby.setLatitude(latitude);
		userNearby.setLongitude(longitude);
		userNearby.setGeohash(GeohashUtil.encode(latitude, longitude));
		userNearby.setLastUpdateTime(new Date().getTime());
		if (userNearbyMapper.getUserNearby(userId) == null) {
			return userNearbyMapper.addUserNearby(userNearby) > 0;
		} else {
			return userNearbyMapper.updateUserNearby(userNearby) > 0;
		}
	}

	/**
	 * 异步执行
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@Override
	public boolean asyncUpdateUserNearBy(final long userId, final double latitude, final double longitude) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				UserNearby userNearby = new UserNearby();
				userNearby.setUserId(userId);
				userNearby.setLatitude(latitude);
				userNearby.setLongitude(longitude);
				userNearby.setGeohash(GeohashUtil.encode(latitude, longitude));
				userNearby.setLastUpdateTime(new Date().getTime());
				if (userNearbyMapper.getUserNearby(userId) == null) {
					userNearbyMapper.addUserNearby(userNearby);
				} else {
					userNearbyMapper.updateUserNearby(userNearby);
				}
			}
		});
		return true;
	}

	/**
	 * 显示附近用户
	 * 
	 */
	@Override
	public List<Profile> showUserNearbyList(long userId) {
		UserNearby userNearby = userNearbyMapper.getUserNearby(userId);
		if (userNearby == null) {
			return null;
		}
		int offset = 0, endIndex = 2;
		String geohash = userNearby.getGeohash().substring(offset, endIndex);
		List<UserNearby> userNearbyList = userNearbyMapper.getUserNearbyList(geohash);
		List<Profile> userNearbyProfiles = new ArrayList<Profile>(userNearbyList.size());
		for (UserNearby userNearbies : userNearbyList) {
			Profile profile = profileMapper.getProfile(userNearbies.getUserId());
			userNearbyProfiles.add(profile);
		}
		return userNearbyProfiles;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserNearbyService#showUserNearByList(double,
	 * double, int, int)
	 */
	@Override
	public List<Profile> showUserNearByList(double latitude, double longitude, int limit, int offset) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserNearbyService#showRddingNearByList(long,
	 * double, double, int, int)
	 */
	public List<RiddingNearby> showRddingNearByList(long userId, double latitude, double longitude, int limit, int offset) {
		String geohash = GeohashUtil.encode(latitude, longitude);
		geohash = geohash.substring(0, geohash.length() - 9);
		List<RiddingNearby> riddingNearBys = riddingNearbyMapper.getRiddingNearbyListByGeo(geohash);
		if (ListUtils.isEmptyList(riddingNearBys) || riddingNearBys.size() < offset - 1) {
			return null;
		}
		return riddingNearBys.subList(offset, offset + limit);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserNearbyService#addRiddingNearbyQuartz()
	 */
	@Override
	public void addRiddingNearbyQuartz() {
		logger.info("addRiddingNearbyQuartz begin");
		int limit = 1;
		List<RiddingNearby> riddingNearbyList = riddingNearbyMapper.getRiddingNearbyList(limit);
		List<Ridding> riddingList;
		if (ListUtils.isEmptyList(riddingNearbyList)) {
			riddingList = riddingMapper.getAllRidding();
		} else {
			riddingList = riddingMapper.getRiddingListByStartRiddingId(riddingNearbyList.get(0).getRiddingId());
		}
		if (ListUtils.isEmptyList(riddingList)) {
			logger.error("Cannot get any riddings!");
			return;
		}
		for (Ridding ridding : riddingList) {
			long mapId = ridding.getMapId();
			IMap iMap = iMapMapper.getRiddingMap(mapId);
			if (iMap == null) {
				continue;
			}
			String mapTaps = iMap.getMapTaps();
			if (StringUtils.isEmpty(mapTaps)) {
				continue;
			}
			RiddingNearby riddingNearby = new RiddingNearby();

			try {
				JSONArray jsonArray = JSONArray.fromObject(mapTaps);
				String mapFirstTap = jsonArray.getString(0);
				int dividePoint = 0, i = 0;
				for (i = 0; i < mapFirstTap.length(); i++) { 
					if (mapFirstTap.charAt(i) == ',') {
						dividePoint = i;
					}
				}
				String lat = mapFirstTap.substring(0, dividePoint);
				double latitude = Double.valueOf(lat);
				String lon = mapFirstTap.substring(dividePoint + 1, mapFirstTap.length());
				double longitude = Double.valueOf(lon);
				riddingNearby.setLatitude(latitude);
				riddingNearby.setLongitude(longitude);
				riddingNearby.setGeohash(GeohashUtil.encode(latitude, longitude));
			} catch (Exception e) {
				logger.error("addRiddingNearbyQuartz error where mapTaps=" + mapTaps);
				e.printStackTrace();
				continue;
			}
			riddingNearby.setRiddingId(ridding.getId());
			riddingNearby.setMapId(mapId);
			riddingNearbyMapper.addRiddingNearby(riddingNearby);
		}
	}
}
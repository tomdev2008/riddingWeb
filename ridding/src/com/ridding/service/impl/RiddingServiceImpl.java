package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.constant.RiddingQuitConstant;
import com.ridding.constant.SourceType;
import com.ridding.mapper.IMapMapper;
import com.ridding.mapper.MapFixMapper;
import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.RiddingActionMapper;
import com.ridding.mapper.RiddingGpsMapper;
import com.ridding.mapper.RiddingMapper;
import com.ridding.mapper.RiddingNearbyMapper;
import com.ridding.mapper.RiddingPictureMapper;
import com.ridding.mapper.RiddingUserMapper;
import com.ridding.mapper.SourceAccountMapper;
import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;
import com.ridding.meta.Profile;
import com.ridding.meta.Public;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingAction;
import com.ridding.meta.RiddingGps;
import com.ridding.meta.RiddingNearby;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.Public.PublicType;
import com.ridding.meta.Ridding.RiddingStatus;
import com.ridding.meta.Ridding.RiddingType;
import com.ridding.meta.RiddingAction.RiddingActionResponse;
import com.ridding.meta.RiddingAction.RiddingActions;
import com.ridding.meta.RiddingUser.RiddingUserRoleType;
import com.ridding.meta.RiddingUser.SelfRiddingStatus;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.meta.vo.ProfileVO;
import com.ridding.service.IOSApnsService;
import com.ridding.service.PublicService;
import com.ridding.service.RiddingService;
import com.ridding.service.UserNearbyService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;
import com.ridding.util.TimeUtil;
import com.ridding.util.http.RiddingUserCache;

/**
 * @author zhengyisheng E-mail:zhengyisheng@corp.netease.com
 * @version CreateTime 2012-3-5 11:56:30 Class Description
 */
@Service("riddingService")
public class RiddingServiceImpl implements RiddingService {

	@Resource
	private IMapMapper mapMapper;

	@Resource
	private RiddingMapper riddingMapper;

	@Resource
	private RiddingUserMapper riddingUserMapper;

	@Resource
	private ProfileMapper profileMapper;

	@Resource
	private MapFixMapper mapFixMapper;

	@Resource
	private TransactionService transactionService;

	@Resource
	private RiddingNearbyMapper riddingNearbyMapper;

	@Resource
	private RiddingPictureMapper riddingPictureMapper;

	@Resource
	private PublicService publicService;
	@Resource
	private SourceAccountMapper sourceAccountMapper;

	@Resource
	private RiddingActionMapper riddingActionMapper;

	@Resource
	private UserNearbyService userNearbyService;

	@Resource
	private RiddingGpsMapper riddingGpsMapper;

	@Resource
	private IOSApnsService iosApnsService;
	private static final Logger logger = Logger.getLogger(RiddingServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getAllRiddingUserList(long,
	 * com.ridding.meta.RiddingUser)
	 */
	@Override
	public List<RiddingUser> getAllRiddingUserList(RiddingUser riddingUser) {
		if (riddingUser == null) {
			return null;
		}
		Profile profile = profileMapper.getProfile(riddingUser.getUserId());
		riddingUser.setProfile(profile);
		this.updateUserInCache(riddingUser);

		List<RiddingUser> riddingUserList = new ArrayList<RiddingUser>();
		if (!riddingUser.isShowTeamer()) {
			riddingUser.setTimeBefore(TimeUtil.getTimeago(riddingUser.getCacheTime(), false));
			riddingUserList.add(riddingUser);
			return riddingUserList;
		} else {
			ConcurrentHashMap<String, RiddingUser> map = RiddingUserCache.getRiddingMap(riddingUser.getRiddingId());
			if (MapUtils.isEmpty(map)) {
				return null;
			}
			Iterator<RiddingUser> iterator = map.values().iterator();
			while (iterator.hasNext()) {
				RiddingUser user = iterator.next();
				user.setTimeBefore(TimeUtil.getTimeago(user.getCacheTime(), false));
				user.setState();
				riddingUserList.add(user);
			}
		}
		return riddingUserList;
	}

	/**
	 * 更新用户当前骑行状态到缓存中
	 * 
	 * @param riddingId
	 * @param riddingUser
	 */
	private void updateUserInCache(RiddingUser riddingUser) {
		if (riddingUser == null) {
			return;
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("latitude", MapFix.getLatPrefix(riddingUser.getLatitude()));
		hashMap.put("longtitude", MapFix.getLngPrefix(riddingUser.getLongtitude()));
		MapFix mapFix = mapFixMapper.getMapFixByLatLng(hashMap);
		if (mapFix != null) {
			mapFix.setRealLat(riddingUser.getLatitude());
			mapFix.setRealLng(riddingUser.getLongtitude());
			riddingUser.setLatitude(mapFix.getLatitude());
			riddingUser.setLongtitude(mapFix.getLongtitude());
		}
		String userKey = RiddingUserCache.createUserKey(riddingUser.getRiddingId(), riddingUser.getUserId());
		RiddingUserCache.set(riddingUser.getRiddingId(), userKey, riddingUser);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingUser(long, long)
	 */
	@Override
	public RiddingUser getRiddingUser(long riddingId, long userId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("riddingId", riddingId);
		hashMap.put("userId", userId);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		return riddingUserMapper.getRiddingUser(hashMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#updateRiddingName(long, long,
	 * java.lang.String)
	 */
	@Override
	public boolean updateRiddingName(long ridingId, long userId, String name) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("riddingId", ridingId);
		hashMap.put("name", name);
		return riddingUserMapper.updateRiddingName(hashMap) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#endRidding(long, long)
	 */
	@Override
	public boolean endRidding(long ridingId, long userId) {
		Ridding ridding = riddingMapper.getRidding(ridingId);
		if (ridding == null) {
			return false;
		}
		IMap iMap = mapMapper.getRiddingMap(ridding.getMapId());
		if (iMap == null) {
			return false;
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("ridding", ridingId);
		hashMap.put("status", SelfRiddingStatus.Finished);
		hashMap.put("totalDistance", iMap.getDistance());
		if (riddingUserMapper.updateRiddingStatus(hashMap) > 0) {
			profileMapper.incUserTotalDistance(hashMap);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRidding(long)
	 */
	@Override
	public Ridding getRidding(long id) {
		return riddingMapper.getRidding(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingList(long, int, long)
	 */
	public List<RiddingUser> getRiddingUserList(long userId, int limit, long createTime, boolean isLarger) {
		if (createTime < 0) {
			createTime = new Date().getTime();
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		// 改用最后更新时间
		hashMap.put("lastUpdateTime", createTime);
		hashMap.put("limit", limit);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		hashMap.put("isLarger", isLarger ? 1 : 0);
		return riddingUserMapper.getSelfRiddingList(hashMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingList(long, int, long)
	 */
	@Override
	public List<ActivityRidding> getSelfRiddingUserList(long userId, int limit, long createTime, boolean isLarger) {
		List<RiddingUser> riddingUsers = this.getRiddingUserList(userId, limit, createTime, isLarger);
		if (ListUtils.isEmptyList(riddingUsers)) {
			return null;
		}
		List<Long> ids = new ArrayList<Long>(riddingUsers.size());
		List<ActivityRidding> activityList = new ArrayList<ActivityRidding>(riddingUsers.size());
		for (RiddingUser riddingUser : riddingUsers) {
			ids.add(riddingUser.getRiddingId());
			ActivityRidding activityRidding = new ActivityRidding();
			activityRidding.setRiddingUser(riddingUser);
			activityList.add(activityRidding);
		}
		List<Ridding> riddingList = riddingMapper.getRiddingList(ids);
		if (ListUtils.isEmptyList(riddingList)) {
			return null;
		}
		this.insertMessage(riddingList, activityList, userId);
		return activityList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#getRiddingListByRiddingId(java.util
	 * .List)
	 */
	public List<ActivityRidding> getRiddingListByRiddingId(List<Long> ids) {
		List<Ridding> riddingList = riddingMapper.getRiddingList(ids);
		if (ListUtils.isEmptyList(riddingList)) {
			return null;
		}
		List<ActivityRidding> activityList = new ArrayList<ActivityRidding>(riddingList.size());
		List<Long> mapIds = new ArrayList<Long>(riddingList.size());
		List<Long> leaderUserIds = new ArrayList<Long>(riddingList.size());
		for (Ridding ridding : riddingList) {
			ActivityRidding activityRidding = new ActivityRidding();
			activityRidding.setRidding(ridding);
			activityList.add(activityRidding);
			mapIds.add(ridding.getMapId());
			leaderUserIds.add(ridding.getLeaderUserId());
		}
		List<Profile> leaderProfileList = profileMapper.getProfileList(leaderUserIds);
		List<IMap> iMapList = mapMapper.getIMaplist(mapIds);
		Map<Long, IMap> iMapMap = HashMapMaker.listToMap(iMapList, "getId", IMap.class);
		Map<Long, Profile> profileMap = HashMapMaker.listToMap(leaderProfileList, "getUserId", Profile.class);
		for (ActivityRidding activityRidding : activityList) {
			Ridding ridding = new Ridding();
			IMap iMap = iMapMap.get(activityRidding.getRidding().getMapId());
			if (iMap != null) {
				activityRidding.setiMap(iMap);
			}
			if (activityRidding.getRidding().getRiddingType() == RiddingType.ShortWay.getValue()) {
//				Map<String, Object> hashMap = new HashMap<String, Object>();
//				hashMap.put("userId", ridding.getLeaderUserId());
//				hashMap.put("riddingId", ridding.getId());
//				hashMap.put("userRole", RiddingUserRoleType.User.intValue());
//				RiddingUser riddingUser = riddingUserMapper.getRiddingUser(hashMap);
//				if (riddingUser != null) {
//					RiddingGps riddingGps = riddingGpsMapper.getRiddingGps(riddingUser.getUserId(), riddingUser.getRiddingId());
//					riddingUser.setRiddingGps(riddingGps);
//					activityRidding.setRiddingUser(riddingUser);
//				}
			}
			Profile profile = profileMap.get(activityRidding.getRidding().getLeaderUserId());
			if (profile != null) {
				activityRidding.setLeaderProfile(profile);
			}
		}
		return activityList;
	}

	/**
	 * 插入骑行的地图信息,骑行信息
	 * 
	 * @param riddingList
	 */
	private void insertMessage(List<Ridding> riddingList, List<ActivityRidding> activityRiddings, long userId) {

		List<Long> mapIds = new ArrayList<Long>(riddingList.size());
		List<Long> leaderUserIds = new ArrayList<Long>(riddingList.size());
		for (Ridding ridding : riddingList) {
			mapIds.add(ridding.getMapId());
			leaderUserIds.add(ridding.getLeaderUserId());
		}
		List<Profile> leaderProfileList = profileMapper.getProfileList(leaderUserIds);
		List<IMap> iMapList = mapMapper.getIMaplist(mapIds);
		Map<Long, IMap> iMapMap = HashMapMaker.listToMap(iMapList, "getId", IMap.class);
		Map<Long, Ridding> riddingMap = HashMapMaker.listToMap(riddingList, "getId", Ridding.class);
		Map<Long, Profile> profileMap = HashMapMaker.listToMap(leaderProfileList, "getUserId", Profile.class);
		for (ActivityRidding activityRidding : activityRiddings) {
			Ridding ridding = riddingMap.get(activityRidding.getRiddingUser().getRiddingId());
			if (ridding != null) {
				activityRidding.setRidding(ridding);
				if (ridding.getRiddingType() == RiddingType.ShortWay.getValue() && activityRidding.getRiddingUser() != null) {
					//取列表不需要返回
//					RiddingGps riddingGps = riddingGpsMapper.getRiddingGps(userId, ridding.getId());
//					if (riddingGps != null) {
//						activityRidding.getRiddingUser().setRiddingGps(riddingGps);
//					}
				} else {
					IMap iMap = iMapMap.get(ridding.getMapId());
					if (iMap != null) {
						activityRidding.setiMap(iMap);
					}
				}

				Profile profile = profileMap.get(ridding.getLeaderUserId());
				if (profile != null) {
					activityRidding.setLeaderProfile(profile);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingUserList(long, int,
	 * int)
	 */
	@Override
	public List<ProfileVO> getRiddingUserListToProfile(long riddingId, int limit, int createTime) {
		List<ProfileVO> profileVOs = new ArrayList<ProfileVO>();
		List<Long> userIdList = this.getProfileByRiddingUserList(riddingId, limit, createTime, profileVOs);
		if (ListUtils.isEmptyList(userIdList)) {
			return null;
		}
		List<Profile> profileList = profileMapper.getProfileList(userIdList);
		if (ListUtils.isEmptyList(profileList)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", SourceType.SINAWEIBO.getValue());
		map.put("userIds", userIdList);
		List<SourceAccount> sourceAccounts = sourceAccountMapper.getSourceAccountByUserIdsSourceType(map);
		Map<Long, SourceAccount> sourceAccountMap = HashMapMaker.listToMap(sourceAccounts, "getUserId", SourceAccount.class);
		Map<Long, Profile> profileMap = HashMapMaker.listToMap(profileList, "getUserId", Profile.class);
		for (ProfileVO profileVO : profileVOs) {
			Profile profile = profileMap.get(profileVO.getUserId());
			if (profile != null) {
				profileVO.setbAvatorUrl(profile.getbAvatorUrl());
				profileVO.setsAvatorUrl(profile.getsAvatorUrl());
				profileVO.setNickName(profile.getNickName());
				profileVO.setGraySAvatorUrl(profile.getGraysAvatorUrl());
			}
			SourceAccount sourceAccount = sourceAccountMap.get(profileVO.getUserId());
			if (sourceAccount != null) {
				profileVO.setSourceAccount(sourceAccount);
			}
		}
		return profileVOs;
	}

	/**
	 * 得到骑行用户的信息
	 * 
	 * @param riddingId
	 * @param limit
	 * @param createTime
	 * @return
	 */
	private List<Long> getProfileByRiddingUserList(long riddingId, int limit, int createTime, List<ProfileVO> profileVOs) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("riddingId", riddingId);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		hashMap.put("createTime", createTime);
		hashMap.put("limit", limit);
		List<RiddingUser> riddingUserList = riddingUserMapper.getRiddingUserListByRiddingId(hashMap);
		if (ListUtils.isEmptyList(riddingUserList)) {
			return null;
		}
		List<Long> userIdList = new ArrayList<Long>(riddingUserList.size());
		for (RiddingUser riddingUser : riddingUserList) {
			ProfileVO profileVO = new ProfileVO();
			profileVO.setUserRole(riddingUser.getUserRole());
			profileVO.setUserId(riddingUser.getUserId());
			profileVOs.add(profileVO);
			userIdList.add(riddingUser.getUserId());
		}
		return userIdList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#updateRiddingUsers(java.util.List,
	 * long)
	 */
	@Override
	public boolean insertRiddingUsers(List<Profile> profileList, long riddingId, int sourceType, long userId) {
		if (ListUtils.isEmptyList(profileList)) {
			return true;
		}
		Profile leaderProfile = profileMapper.getProfile(userId);
		Ridding ridding = riddingMapper.getRidding(riddingId);
		if (ridding == null) {
			return false;
		}
		int succCount = 0;
		for (Profile profile : profileList) {
			try {
				if (transactionService.insertRiddingUser(ridding, profile, sourceType, leaderProfile)) {
					succCount++;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("insertRiddingUsers error where sinaId=" + profile.getAccessUserId());
			}
		}
		logger.info("insertRiddingUsers successCount=" + succCount + " and initCount=" + profileList.size());
		return succCount > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#deleteRiddingUsers(java.util.List,
	 * long)
	 */
	@Override
	public boolean deleteRiddingUsers(List<Long> userIdList, long riddingId) {
		if (ListUtils.isEmptyList(userIdList)) {
			logger.info("deleteRiddingUsers no sinaUseridList");
			return false;
		}
		Ridding ridding = riddingMapper.getRidding(riddingId);
		if (ridding == null) {
			return false;
		}
		int succCount = 0;
		Map<String, Object> hashMap = new HashMap<String, Object>();
		for (Long userid : userIdList) {
			hashMap.put("userId", userid);
			hashMap.put("riddingId", riddingId);
			hashMap.put("userRole", RiddingUserRoleType.Nothing.intValue());
			if (riddingUserMapper.updateRiddingUserRole(hashMap) > 0) {
				hashMap.put("id", riddingId);
				hashMap.put("count", -1);
				riddingMapper.increaseUserCount(hashMap);
				succCount++;
			}
		}
		logger.info("deleteRiddingUsers successCount=" + succCount + " and initCount=" + userIdList.size());
		return succCount > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#quitRidding(long, long)
	 */
	@Override
	public RiddingQuitConstant quitRidding(long userId, long riddingId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("riddingId", riddingId);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());

		RiddingUser riddingUser = riddingUserMapper.getRiddingUser(hashMap);
		if (riddingUser == null) {
			return RiddingQuitConstant.Failed;
		}
		hashMap.put("id", riddingId);
		if (riddingUser.isJustATeamer()) {
			hashMap.put("userRole", RiddingUserRoleType.Nothing.intValue());
			hashMap.put("count", -1);
			riddingMapper.increaseUserCount(hashMap);
			if (riddingUserMapper.updateRiddingUserRole(hashMap) > 0) {
				return RiddingQuitConstant.Success;
			}
		}
		if (riddingUser.isLeader()) {
			Ridding ridding = riddingMapper.getRidding(riddingId);
			if (ridding != null) {
				int count = ridding.getUserCount();
				if (count != 1) {
					return RiddingQuitConstant.Leader;
				}
				// 队长自身在ridding_User中退出，就是删除
				hashMap.put("riddingStatus", RiddingStatus.Deleted.getValue());
				if (riddingUserMapper.updateRiddingStatus(hashMap) < 0) {
					return RiddingQuitConstant.Failed;
				}
				riddingNearbyMapper.deleteRiddingNearBy(riddingId);
				// 在ridding 表中退出
				if (riddingMapper.updateRiddingStatus(hashMap) > 0) {
					return RiddingQuitConstant.Success;
				}
			}
		}
		return RiddingQuitConstant.Failed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#endRiddingByLeader(long, long)
	 */
	@Override
	public boolean endRiddingByLeader(long riddingId, long userId) {
		Ridding ridding = riddingMapper.getRidding(riddingId);
		if (ridding == null || ridding.getLeaderUserId() != userId) {
			return false;
		}
		IMap iMap = mapMapper.getRiddingMap(ridding.getMapId());
		if (iMap == null) {
			return false;
		}
		try {
			return transactionService.updateEndRiddingByLeader(riddingId, iMap.getDistance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingCount(long)
	 */
	@Override
	public int getRiddingCount(long userId) {
		return riddingUserMapper.getRiddingCount(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingListbyUserId(long, int,
	 * int)
	 */
	@Override
	public List<ActivityRidding> getRiddingListbyUserId(long userId, int limit, int offset) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("offset", offset);
		hashMap.put("limit", limit);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		List<RiddingUser> riddingUsers = riddingUserMapper.getRiddingListByUserId(hashMap);
		if (ListUtils.isEmptyList(riddingUsers)) {
			return null;
		}
		List<Long> ids = new ArrayList<Long>(riddingUsers.size());
		List<ActivityRidding> activityList = new ArrayList<ActivityRidding>(riddingUsers.size());
		for (RiddingUser riddingUser : riddingUsers) {
			ids.add(riddingUser.getRiddingId());
			ActivityRidding activityRidding = new ActivityRidding();
			activityRidding.setRiddingUser(riddingUser);
			activityList.add(activityRidding);
		}
		List<Ridding> riddingList = riddingMapper.getRiddingList(ids);
		if (ListUtils.isEmptyList(riddingList)) {
			return null;
		}
		this.insertMessage(riddingList, activityList, userId);
		return activityList;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingsbyUserId(long)
	 */
	@Override
	public List<Ridding> getRiddingsbyUserId(long userId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("userId", userId);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		List<RiddingUser> riddingUsers = riddingUserMapper.getRiddingUsersByUserId(hashMap);
		if (ListUtils.isEmptyList(riddingUsers)) {
			return null;
		}
		List<Long> riddingIds = new ArrayList<Long>(riddingUsers.size());
		for (RiddingUser riddingUser : riddingUsers) {
			riddingIds.add(riddingUser.getRiddingId());
		}
		List<Ridding> riddings = riddingMapper.getRiddingList(riddingIds);
		Map<Long, Ridding> riddingHashMap = HashMapMaker.listToMap(riddings, "getId", Ridding.class);
		List<Ridding> riddinglist = new ArrayList<Ridding>(riddings.size());
		for (long riddingId : riddingIds) {
			Ridding tmpRidding = riddingHashMap.get(riddingId);
			if (tmpRidding != null) {
				riddinglist.add(tmpRidding);
			}
		}
		return riddinglist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#addRidding(com.ridding.meta.Ridding)
	 */
	@Override
	public Ridding addRidding(Ridding ridding) {
		if (riddingMapper.addRidding(ridding) > 0) {
			RiddingUser riddingUser = new RiddingUser();
			riddingUser.setRiddingId(ridding.getId());
			riddingUser.setCreateTime(ridding.getCreateTime());
			riddingUser.setLastUpdateTime(ridding.getCreateTime());
			riddingUser.setUserId(ridding.getLeaderUserId());
			riddingUser.setUserRole(RiddingUserRoleType.Leader.intValue());
			riddingUser.setSelfName(ridding.getName());
			riddingUser.setRiddingStatus(SelfRiddingStatus.Beginning.getValue());
			riddingUserMapper.addRiddingUser(riddingUser);
			return ridding;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#addRiddingPicture(com.ridding.meta
	 * .RiddingPicture)
	 */
	@Override
	public boolean addRiddingPicture(RiddingPicture riddingPicture) {
		Profile profile = profileMapper.getProfile(riddingPicture.getUserId());
		Ridding ridding = riddingMapper.getRidding(riddingPicture.getRiddingId());
		List<RiddingAction> actions = riddingActionMapper.getRiddingActionsByType(riddingPicture.getRiddingId(), RiddingActions.Care.getValue());
		if (!ListUtils.isEmptyList(actions)) {
			for (RiddingAction action : actions) {
				iosApnsService.sendUserApns(action.getUserId(), profile.getUserName() + "更新了他的骑行活动:" + ridding.getName() + "赶快去看下吧^^");
			}
		}
		if (riddingPicture.getTakePicDate() == 0) {
			riddingPicture.setTakePicDate(new Date().getTime());
		}
		if (riddingPictureMapper.addRiddingPicture(riddingPicture) > 0) {
			riddingMapper.incPictureCount(riddingPicture.getRiddingId());
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#getRiddingPictureByUserIdRiddingId
	 * (long, long)
	 */
	@Override
	public List<RiddingPicture> getRiddingPictureByRiddingId(long riddingId, int limit, long lastUpdateTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("riddingId", riddingId);
		map.put("createTime", lastUpdateTime);
		map.put("limit", limit);
		return riddingPictureMapper.getRiddingPicturesByRiddingId(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRecomRiddingList(int, int,
	 * java.lang.Boolean)
	 */
	@Override
	public List<Ridding> getRecomRiddingList(int weight, int limit, Boolean isLarger) {
		List<Public> publicList = publicService.getPublicListByType(PublicType.PublicRecom.getValue(), limit, weight, isLarger);

		if (!ListUtils.isEmptyList(publicList)) {
			List<Ridding> riddingList = new ArrayList<Ridding>(publicList.size());
			for (Public aPublic : publicList) {
				aPublic.getJson();
				Ridding newRidding = riddingMapper.getRidding(aPublic.getRiddingId());
				if (newRidding == null) {
					continue;
				}
				newRidding.setaPublic(aPublic);
				riddingList.add(newRidding);
			}
			this.insertRiddingInfo(riddingList);
			return riddingList;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#setRiddingIsRecom(long)
	 */
	@Override
	public boolean setRiddingIsRecom(long riddingId) {
		Public public1 = new Public();
		long nowTime = new Date().getTime();
		public1.setCreateTime(nowTime);
		public1.setLastUpdateTime(nowTime);
		public1.setRiddingId(riddingId);
		public1.setType(PublicType.PublicRecom.getValue());
		public1.genJson();
		public1.setWeight(1);
		publicService.addPublic(public1);
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", riddingId);
		hashMap.put("isRecom", Ridding.PublicOrRecom);
		return riddingMapper.updateRecomStatus(hashMap) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#setRiddingIsNotRecom(long, long)
	 */
	public boolean setRiddingIsNotRecom(long publicId, long riddingId) {
		publicService.deletePublicByPublicId(publicId);
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", riddingId);
		hashMap.put("isRecom", Ridding.notPublicOrRecom);
		return riddingMapper.updateRecomStatus(hashMap) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#getRiddingListByLastUpdateTime(long,
	 * int, com.ridding.meta.Ridding.RiddingStatus)
	 */
	@Override
	public List<Ridding> getRiddingListByLastUpdateTime(long lastUpdateTime, int limit, Boolean isLarger, int isRecom) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastUpdateTime", lastUpdateTime);
		map.put("limit", limit);
		map.put("isLarger", isLarger ? 1 : 0);
		List<Ridding> riddingList = riddingMapper.getRiddingListByLastUpdateTime(map);
		if (ListUtils.isEmptyList(riddingList)) {
			return null;
		}
		// 如果取的数据的时间是大于lastUpdateTime，得到的是增序排列的值，需要做转换成降序
		if (isLarger) {
			List<Ridding> descRiddingList = new ArrayList<Ridding>(riddingList.size());
			for (int i = riddingList.size() - 1; i >= 0; i--) {
				descRiddingList.add(riddingList.get(i));
			}
			riddingList = descRiddingList;
		}
		this.insertRiddingInfo(riddingList);
		return riddingList;
	}

	/**
	 * 插入骑行信息
	 * 
	 * @param riddingList
	 */
	private void insertRiddingInfo(List<Ridding> riddingList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> mapIds = new ArrayList<Long>(riddingList.size());
		List<Long> leaderUserIds = new ArrayList<Long>(riddingList.size());
		for (Ridding ridding : riddingList) {
			mapIds.add(ridding.getMapId());
			leaderUserIds.add(ridding.getLeaderUserId());
		}
		if (ListUtils.isEmptyList(mapIds)) {
			return;
		}
		List<Profile> profileList = profileMapper.getProfileList(leaderUserIds);
		Map<Long, Profile> profileMap = HashMapMaker.listToMap(profileList, "getUserId", Profile.class);
		List<IMap> iMapList = mapMapper.getIMaplist(mapIds);
		Map<Long, IMap> iMapMap = HashMapMaker.listToMap(iMapList, "getId", IMap.class);
		if (!ListUtils.isEmptyList(riddingList)) {
			for (Ridding ridding : riddingList) {
				Profile profile = profileMap.get(ridding.getLeaderUserId());
				if (profile != null) {
					ridding.setLeaderProfile(profile);
				}

				map.put("riddingId", ridding.getId());
				map.put("createTime", 0);
				map.put("limit", 1);
				IMap iMap = iMapMap.get(ridding.getMapId());
				if (iMap != null) {
					ridding.setDistance(iMap.getDistance());
				}
				if (ridding.getaPublic() == null) {
					Public public1 = new Public();
					ridding.setaPublic(public1);
				}
				if (StringUtils.isEmpty(ridding.getaPublic().getFirstPicUrl())) {
					List<RiddingPicture> list = riddingPictureMapper.getRiddingPicturesByRiddingId(map);
					if (!ListUtils.isEmptyList(list)) {
						RiddingPicture riddingPicture = list.get(0);
						ridding.getaPublic().setFirstPicUrl(riddingPicture.getPhotoUrl());
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#incRiddingLike(long)
	 */
	@Override
	public RiddingActionResponse incRiddingLike(long riddingId, long userId) {
		if (this.checkIsInRiddingAction(riddingId, userId, RiddingActions.Like)) {
			return RiddingActionResponse.DoubleDo;
		}

		if (this.checkIsInRidding(riddingId, userId)) {
			return RiddingActionResponse.InRidding;
		}

		this.addRiddingAction(riddingId, userId, RiddingActions.Like.getValue());
		if (riddingMapper.incLikeCount(riddingId) > 0) {
			return RiddingActionResponse.SUCC;
		}
		return RiddingActionResponse.Fail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#incRiddingUse(long, long)
	 */
	@Override
	public RiddingActionResponse incRiddingUse(long riddingId, long userId) {
		if (this.checkIsInRiddingAction(riddingId, userId, RiddingActions.Use)) {
			return RiddingActionResponse.DoubleDo;
		}

		if (this.checkIsInRidding(riddingId, userId)) {
			return RiddingActionResponse.InRidding;
		}
		this.addRiddingAction(riddingId, userId, RiddingActions.Use.getValue());
		if (riddingMapper.incUseCount(riddingId) > 0) {
			return RiddingActionResponse.SUCC;
		}
		return RiddingActionResponse.Fail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#incRiddingCare(long, long)
	 */
	@Override
	public RiddingActionResponse incRiddingCare(long riddingId, long userId) {

		if (this.checkIsInRiddingAction(riddingId, userId, RiddingActions.Care)) {
			return RiddingActionResponse.DoubleDo;
		}

		if (this.checkIsInRidding(riddingId, userId)) {
			return RiddingActionResponse.InRidding;
		}

		this.addRiddingAction(riddingId, userId, RiddingActions.Care.getValue());
		if (riddingMapper.incCareCount(riddingId) > 0) {
			return RiddingActionResponse.SUCC;
		}
		return RiddingActionResponse.Fail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#checkIsInRiddingAction(long,
	 * long, com.ridding.meta.RiddingAction.RiddingActions)
	 */
	@Override
	public boolean checkIsInRiddingAction(long riddingId, long userId, RiddingActions action, long objectId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("riddingId", riddingId);
		hashMap.put("userId", userId);
		hashMap.put("type", action.getValue());
		hashMap.put("objectId", objectId);
		if (riddingActionMapper.getRiddingActionByUserId(hashMap) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkIsInRiddingAction(long riddingId, long userId, RiddingActions action) {
		return this.checkIsInRiddingAction(riddingId, userId, action, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#checkIsInRidding(long, long)
	 */
	@Override
	public boolean checkIsInRidding(long riddingId, long userId) {
		List<RiddingUser> riddingUsers = this.getAllRiddingUsers(riddingId);
		if (ListUtils.isEmptyList(riddingUsers)) {
			return false;
		}
		for (RiddingUser riddingUser : riddingUsers) {
			if (riddingUser.getUserId() == userId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到所有队员
	 * 
	 * @param riddingId
	 * @return
	 */
	private List<RiddingUser> getAllRiddingUsers(long riddingId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("riddingId", riddingId);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		hashMap.put("createTime", new Date().getTime());
		hashMap.put("limit", -1);
		return riddingUserMapper.getRiddingUserListByRiddingId(hashMap);
	}

	/**
	 * 添加操作记录
	 * 
	 * @param riddingId
	 * @param userId
	 * @param type
	 * @return
	 */
	private boolean addRiddingAction(long riddingId, long userId, int type) {
		RiddingAction riddingAction = new RiddingAction();
		riddingAction.setUserId(userId);
		riddingAction.setRiddingId(riddingId);
		riddingAction.setType(type);
		long nowTime = new Date().getTime();
		riddingAction.setCreateTime(nowTime);
		riddingAction.setLastUpdateTime(nowTime);
		return riddingActionMapper.addRiddingAction(riddingAction) > 0;
	}

	/**
	 * 根据Object得到操作
	 * 
	 * @param riddingId
	 * @param userId
	 * @param type
	 * @param objectId
	 * @return
	 */
	private RiddingAction getRiddingActionByObject(long riddingId, long userId, int type, long objectId) {
		RiddingAction riddingAction = new RiddingAction();
		riddingAction.setUserId(userId);
		riddingAction.setRiddingId(riddingId);
		riddingAction.setType(type);
		riddingAction.setObjectId(objectId);
		long nowTime = new Date().getTime();
		riddingAction.setCreateTime(nowTime);
		riddingAction.setLastUpdateTime(nowTime);
		return riddingAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingPictureList(long, long)
	 */
	@Override
	public List<RiddingPicture> getRiddingPictureList(long riddingId, long userId, int limit, long createTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("riddingId", riddingId);
		map.put("createTime", new Date().getTime());
		map.put("limit", 1);
		return riddingPictureMapper.getRiddingPicturesByRiddingId(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getUserAction(long, long)
	 */
	@Override
	public RiddingAction getUserAction(long userId, long riddingId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("riddingId", riddingId);
		hashMap.put("userId", userId);
		List<RiddingAction> riddingactionList = riddingActionMapper.getRiddingActionsByUserId(hashMap);
		RiddingAction riddingAction = new RiddingAction();
		if (ListUtils.isEmptyList(riddingactionList)) {
			riddingAction.setUserCared(false);
			riddingAction.setUserLiked(false);
			riddingAction.setUserUsed(false);
			return riddingAction;
		}
		for (RiddingAction action : riddingactionList) {
			if (RiddingActions.genRiddingAction(action.getType()) == RiddingActions.Care) {
				riddingAction.setUserCared(true);
			} else if (RiddingActions.genRiddingAction(action.getType()) == RiddingActions.Use) {
				riddingAction.setUserUsed(true);
			} else if (RiddingActions.genRiddingAction(action.getType()) == RiddingActions.Like) {
				riddingAction.setUserLiked(true);
			}
		}
		return riddingAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#incRiddingComment(long)
	 */
	@Override
	public boolean incRiddingComment(long riddingId) {
		return riddingMapper.incCommentCount(riddingId) > 0;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingsbyLike(int)
	 */
	@Override
	public List<Ridding> getRiddingsbyLike(int limit, int offset) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("limit", limit);
		hashMap.put("offset", offset);
		List<Ridding> riddingList = riddingMapper.getRiddingsbyLike(hashMap);
		this.insertRiddingInfo(riddingList);
		return riddingList;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingsbyComment(int)
	 */
	@Override
	public List<Ridding> getRiddingsbyComment(int limit, int offset) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("limit", limit);
		hashMap.put("offset", offset);
		List<Ridding> riddingList = riddingMapper.getRiddingsbyComment(hashMap);
		this.insertRiddingInfo(riddingList);
		return riddingList;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingsbyUse(int)
	 */
	@Override
	public List<Ridding> getRiddingsbyUse(int limit, int offset) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("limit", limit);
		hashMap.put("offset", offset);
		List<Ridding> riddingList = riddingMapper.getRiddingsbyUse(hashMap);
		this.insertRiddingInfo(riddingList);
		return riddingList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingsbyPicture(int, int)
	 */
	public List<Ridding> getRiddingsbyPicture(int limit, int offset) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("limit", limit);
		hashMap.put("offset", offset);
		List<Ridding> riddingList = riddingMapper.getRiddingsbyPicture(hashMap);
		this.insertRiddingInfo(riddingList);
		return riddingList;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#incPicLike(long, long, long)
	 */
	@Override
	public RiddingActionResponse incPicLike(long riddingId, long userId, long objectId) {
		if (this.checkIsInRiddingAction(riddingId, userId, RiddingActions.LikePicture, objectId)) {
			return RiddingActionResponse.DoubleDo;
		}

		if (this.checkIsInRidding(riddingId, userId)) {
			return RiddingActionResponse.InRidding;
		}

		if (riddingPictureMapper.incLikePicCount(objectId) > 0) {
			if (riddingActionMapper.addRiddingAction(this
					.getRiddingActionByObject(riddingId, userId, RiddingActions.LikePicture.getValue(), objectId)) > 0) {
				return RiddingActionResponse.SUCC;
			}
		}
		return RiddingActionResponse.Fail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingActionsByType(long,
	 * int)
	 */
	@Override
	public List<RiddingAction> getRiddingActionsByType(long riddingId, int type) {
		return riddingActionMapper.getRiddingActionsByType(riddingId, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#getRiddingActionsByTypeUserId(long,
	 * int, long)
	 */
	public List<RiddingAction> getRiddingActionsByTypeUserId(long riddingId, int type, long userId) {
		if (userId > 0) {
			return riddingActionMapper.getRiddingActionsByTypeUserId(riddingId, type, userId);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see com.ridding.service.RiddingService#removeRiddingPicture(long)
	 */
	@Override
	public boolean removeRiddingPicture(long pictureId) {
		RiddingPicture riddingPicture = riddingPictureMapper.getRiddingPicturesById(pictureId);
		if (riddingPicture == null) {
			return false;
		}
		riddingMapper.decPictureCount(riddingPicture.getRiddingId());
		return riddingPictureMapper.deleteRiddingPicture(pictureId) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingPictureByBreadId(long,
	 * long)
	 */
	@Override
	public RiddingPicture getRiddingPictureByBreadId(long breadId, long riddingId) {
		return riddingPictureMapper.getPictureByBreadId(breadId, riddingId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingPictureById(long)
	 */
	public RiddingPicture getRiddingPictureById(long pictureId) {
		return riddingPictureMapper.getRiddingPicturesById(pictureId);
	}

	/*
	 * (non-Javadoc) 修复程序
	 * 
	 * @see com.ridding.service.RiddingService#fixPictureCount()
	 */
	private boolean fixPictureCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isLarger", 1);
		map.put("lastUpdateTime", 0);
		List<Ridding> riddingList = riddingMapper.getRiddingListByLastUpdateTime(map);
		if (ListUtils.isEmptyList(riddingList)) {
			logger.error("Failed to get riddingList!");
			return false;
		}
		logger.info("Success to get riddingList!");
		for (Ridding ridding : riddingList) {
			Map<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("riddingId", ridding.getId());
			hashMap.put("createTime", 0);
			List<RiddingPicture> riddingPictureList = riddingPictureMapper.getRiddingPicturesByRiddingId(hashMap);
			if (ListUtils.isEmptyList(riddingPictureList)) {
				logger.error("Failed to get riddingPictureList with riddingId = " + ridding.getId());
				continue;
			}
			riddingMapper.updateRiddingPictureCount(ridding.getId(), riddingPictureList.size());
		}
		logger.info("Success to fix the pictureCount!");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getNearByRiddingList(long,
	 * double, double, int, int)
	 */
	@Override
	public List<ActivityRidding> getNearByRiddingList(long userId, double latitude, double longitude, int limit, int offset) {
		List<RiddingNearby> riddingNearbyList = userNearbyService.showRddingNearByList(userId, latitude, longitude, limit, offset);
		List<Long> riddingIdList = new ArrayList<Long>(riddingNearbyList.size());
		for (RiddingNearby riddingNearby : riddingNearbyList) {
			riddingIdList.add(riddingNearby.getRiddingId());
		}
		return this.getRiddingListByRiddingId(riddingIdList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#updateRiddingSyncWifi(long, int)
	 */
	@Override
	public boolean updateRiddingSyncWifi(long riddingId, int isSyncWifi, long userId) {
		return riddingUserMapper.updateRiddingSyncWifi(userId, riddingId, isSyncWifi) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#updateRiddingGps(long, long, int)
	 */
	@Override
	public int updateRiddingGps(long userId, long riddingId, int isGps) {
		return riddingUserMapper.updateRiddingGps(userId, riddingId, isGps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#addRiddingGps(com.ridding.meta.RiddingGps
	 * )
	 */
	@Override
	public RiddingGps addRiddingGps(RiddingGps riddingGps) {
		RiddingGps dbRiddingGps = riddingGpsMapper.getRiddingGps(riddingGps.getUserId(), riddingGps.getRiddingId());
		if (dbRiddingGps != null) {
			dbRiddingGps.setMapPoint(riddingGps.getMapPoint());
			riddingGpsMapper.updateRiddingGpsPoint(riddingGps.getRiddingId(), riddingGps.getUserId(), riddingGps.getMapPoint());
		} else {
			if (riddingGpsMapper.addRiddingGps(riddingGps) > 0) {
				return riddingGps;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingService#getRiddingGps(long, long)
	 */
	@Override
	public RiddingGps getRiddingGps(long userId, long riddingId) {
		return riddingGpsMapper.getRiddingGps(userId, riddingId);
	}
}

package com.ridding.service.transaction;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.transaction.TransactionException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.ridding.constant.SystemConst;
import com.ridding.mapper.CityMapper;
import com.ridding.mapper.IMapMapper;
import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.RepostMapWeiBoMapper;
import com.ridding.mapper.RiddingActionMapper;
import com.ridding.mapper.RiddingCommentMapper;
import com.ridding.mapper.RiddingMapper;
import com.ridding.mapper.RiddingPictureMapper;
import com.ridding.mapper.RiddingUserMapper;
import com.ridding.mapper.SourceAccountMapper;
import com.ridding.meta.City;
import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.RepostMap;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.Source;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.WeiBo;
import com.ridding.meta.Ridding.RiddingStatus;
import com.ridding.meta.RiddingUser.RiddingUserRoleType;
import com.ridding.meta.RiddingUser.SelfRiddingStatus;
import com.ridding.service.IOSApnsService;
import com.ridding.util.FileUtil;
import com.ridding.util.ListUtils;
import com.ridding.util.MD5Util;
import com.ridding.util.QiNiuUtil;
import com.ridding.util.ThumbnailUtil;
import com.ridding.web.controller.RiddingController;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime时间2012-3-21 12:22:34 Class Description 事务service
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
	@Resource
	private SourceAccountMapper sourceAccountMapper;

	@Resource
	private IMapMapper iMapMapper;

	@Resource
	private RiddingMapper riddingMapper;

	@Resource
	private RiddingUserMapper riddingUserMapper;

	@Resource
	private ProfileMapper profileMapper;

	@Resource
	private CityMapper cityMapper;

	@Resource
	private RepostMapWeiBoMapper repostMapWeiBoMapper;

	@Resource
	private RiddingPictureMapper riddingPictureMapper;
	@Resource
	private RiddingActionMapper riddingActionMapper;
	@Resource
	private RiddingCommentMapper riddingCommentMapper;

	@Resource
	private IOSApnsService iosApnsService;

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	private static final Logger logger = Logger.getLogger(RiddingController.class);

	/**
	 * 异步讲小图变灰并且上传
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public boolean asyncgrayAvator(final Profile profile) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				String key = QiNiuUtil.genKey(true, true);
				try {
					String originPath = "TransactionServiceImpl" + new Date().getTime() + "_old.jpg";
					String newPath = "TransactionServiceImpl" + new Date().getTime() + "_new.jpg";
					File file = FileUtil.getFileFromUrl(profile.getsAvatorUrl(), originPath);
					if (file == null) {
						return;
					}
					boolean succ = ThumbnailUtil.grayImageToPath(originPath, newPath);
					if (!succ) {
						return;
					}
					succ = false;
					succ = QiNiuUtil.uploadImageToQiniuFromLocalFile(newPath, key);
					if (succ) {
						profile.setGraysAvatorUrl(SystemConst.returnPhotoUrl("/" + key));
						profileMapper.updategraysAvator(profile);
					}
					if (file.isFile()) {
						file.delete();
					}
					file = new File(newPath);
					if (file.isFile()) {
						file.delete();
					}
				} catch (Exception e) {
					logger.error("ProfileServiceImpl asyncgrayAvator where url=" + profile.getsAvatorUrl());
					e.printStackTrace();
					return;
				}

			}
		});
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.transaction.TransactionService#insertMapCreateRidding
	 * (com.ridding.meta.IMap)
	 */
	public boolean insertMap(IMap iMap, Source source) throws TransactionException {
		if (iMap == null) {
			return false;
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("sourceType", source.getSourceType());
		hashMap.put("accessUserId", source.getAccessUserId());
		SourceAccount account = sourceAccountMapper.getSourceAccountByAccessUserId(hashMap);
		if (account == null) {
			account = new SourceAccount();
			account.setAccessUserId(source.getAccessUserId());
			account.setSourceType(source.getSourceType());
			account.setCreateTime(iMap.getCreateTime());
			this.insertSourceAccount(account, null);
		}
		iMap.setUserId(account.getUserId());
		iMap.setStatus(IMap.NotUsing);
		if (iMapMapper.addRiddingMap(iMap) <= 0) {
			throw new TransactionException("insertMapCreateRidding iMapMapper error");
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.transaction.TransactionService#insertANewRidding(
	 * com.ridding.meta.IMap)
	 */
	public boolean insertANewRidding(IMap iMap, Ridding ridding) {
		List<City> cityList = cityMapper.getCitybyName(iMap.getCityName());
		if (!ListUtils.isEmptyList(cityList)) {
			City city = cityList.get(0);
			iMap.setCityId(city.getId());
		}

		if (iMap.getId() == 0) {
			if (iMapMapper.addRiddingMap(iMap) < 0) {
				throw new TransactionException("insertANewRidding iMapMapper addRiddingMap error");
			}
		} else if (iMapMapper.updateRiddingMap(iMap) < 0) {
			throw new TransactionException("insertANewRidding iMapMapper updateRiddingMap error");
		}
		if (ridding == null) {
			return false;
		}
		ridding.setMapId(iMap.getId());
		ridding.setRiddingStatus(RiddingStatus.Beginning.getValue());
		long nowTime = new Date().getTime();
		ridding.setCreateTime(nowTime);
		ridding.setLastUpdateTime(nowTime);
		ridding.setRiddingType(0);
		ridding.setUserCount(1);
		if (riddingMapper.addRidding(ridding) < 0) {
			throw new TransactionException("insertANewRidding iMapMapper error");
		}
		RiddingUser riddingUser = new RiddingUser();
		riddingUser.setRiddingId(ridding.getId());
		riddingUser.setUserId(ridding.getLeaderUserId());
		riddingUser.setUserRole(RiddingUserRoleType.Leader.intValue());
		riddingUser.setCreateTime(nowTime);
		riddingUser.setLastUpdateTime(nowTime);
		riddingUser.setRiddingStatus(SelfRiddingStatus.Beginning.getValue());
		riddingUser.setSelfName(ridding.getName());
		if (riddingUserMapper.addRiddingUser(riddingUser) < 0) {
			throw new TransactionException("insertANewRidding iMapMapper error");
		}
		List<Profile> profiles = profileMapper.getAllProfile();
		for (Profile profile : profiles) {
			if (StringUtils.isEmpty(profile.getGraysAvatorUrl())) {
				this.asyncgrayAvator(profile);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.transaction.TransactionService#insertRiddingUser(
	 * long, java.util.List)
	 */
	public boolean insertRiddingUser(Ridding ridding, Profile profile, int objectType, Profile leaderProfile) throws TransactionException {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("sourceType", objectType);
		hashMap.put("accessUserId", profile.getAccessUserId());
		SourceAccount sourceAccount = sourceAccountMapper.getSourceAccountByAccessUserId(hashMap);
		if (sourceAccount == null) {
			sourceAccount = new SourceAccount();
			sourceAccount.setAccessUserId(profile.getAccessUserId());
			sourceAccount.setSourceType(objectType);
			sourceAccount.setCreateTime(new Date().getTime());
			this.insertSourceAccount(sourceAccount, profile);
		} else {
			if (leaderProfile != null) {
				String message = leaderProfile.getUserName() + "把你加入了骑行活动:" + ridding.getName() + ",快去看看吧";
				iosApnsService.sendUserApns(sourceAccount.getUserId(), message);
			}
		}
		long nowTime = new Date().getTime();
		hashMap = new HashMap<String, Object>();
		hashMap.put("userId", sourceAccount.getUserId());
		hashMap.put("riddingId", ridding.getId());
		hashMap.put("userRole", RiddingUserRoleType.Nothing.intValue());
		RiddingUser riddingUser = riddingUserMapper.getRiddingUser(hashMap);
		if (riddingUser != null && !riddingUser.isTeamer()) {
			hashMap.put("userRole", RiddingUserRoleType.User.intValue());
			riddingUserMapper.updateRiddingUserRole(hashMap);
		} else if (riddingUser != null && riddingUser.isTeamer()) {
			return true;
		} else {
			riddingUser = new RiddingUser();
			riddingUser.setRiddingId(ridding.getId());
			riddingUser.setUserId(sourceAccount.getUserId());
			riddingUser.setLastUpdateTime(nowTime);
			riddingUser.setCreateTime(nowTime);
			riddingUser.setRiddingStatus(SelfRiddingStatus.Beginning.getValue());
			riddingUser.setSelfName(ridding.getName());
			riddingUser.setUserRole(RiddingUserRoleType.User.intValue());
			if (riddingUserMapper.addRiddingUser(riddingUser) < 0) {
				throw new TransactionException("insertRiddingUser addRiddingUser error ");
			}
		}
		hashMap.put("id", ridding.getId());
		hashMap.put("count", 1);

		if (riddingMapper.increaseUserCount(hashMap) < 0) {
			throw new TransactionException("insertRiddingUser increaseUserCount error ");
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.transaction.TransactionService#insertSourceAccount
	 * (com.ridding.meta.SourceAccount, com.ridding.meta.Profile)
	 */
	public Profile insertSourceAccount(SourceAccount sourceAccount, Profile profile) throws TransactionException {
		if (sourceAccount == null) {
			return null;
		}
		if (profile == null) {
			profile = new Profile();
		}
		try {
			Users users = new Users();
			User user = users.showUserById(String.valueOf(sourceAccount.getAccessUserId()));
			profile.setUserName(user.getName());
			profile.setNickName(user.getName());
			profile.setsAvatorUrl(user.getProfileImageUrl());
			profile.setbAvatorUrl(user.getavatarLarge());
		} catch (WeiboException e) {
			logger.error("insertSourceAccount error");
		}
		profile.setCreateTime(sourceAccount.getCreateTime());
		profile.setLastUpdateTime(sourceAccount.getCreateTime());
		if (profileMapper.addProfile(profile) < 0) {
			throw new TransactionException("insertSourceAccount addProfile error ");
		}
		// 生成taobaoCode，并且添加
		this.genCode(profile);
		profileMapper.updateProfileTaobaoCode(profile.getTaobaoCode(), profile.getUserId());

		if (!StringUtils.isEmpty(profile.getsAvatorUrl())) {
			this.asyncgrayAvator(profile);
		}
		sourceAccount.setUserId(profile.getUserId());
		if (sourceAccountMapper.addSourceAccount(sourceAccount) < 0) {
			throw new TransactionException("insertSourceAccount addSourceAccount error ");
		}

		return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.transaction.TransactionService#endRiddingByLeader
	 * (long)
	 */
	@Override
	public boolean updateEndRiddingByLeader(long riddingId, int distance) throws TransactionException {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", riddingId);
		hashMap.put("riddingStatus", RiddingStatus.Finished.getValue());
		if (riddingMapper.updateRiddingStatus(hashMap) < 0) {
			throw new TransactionException("endRiddingByLeader updateRiddingStatus error !");
		}
		hashMap.put("riddingId", riddingId);
		hashMap.put("userRole", RiddingUserRoleType.User.intValue());
		hashMap.put("createTime", 0);
		hashMap.put("limit", -1);
		List<RiddingUser> riddingUsers = riddingUserMapper.getRiddingUserListByRiddingId(hashMap);
		if (!ListUtils.isEmptyList(riddingUsers)) {
			for (RiddingUser riddingUser : riddingUsers) {
				hashMap.put("totalDistance", distance);
				hashMap.put("userId", riddingUser.getUserId());
				riddingUserMapper.updateRiddingStatus(hashMap);
				if (profileMapper.incUserTotalDistance(hashMap) < 0) {
					throw new TransactionException("endRiddingByLeader incUserTotalDistance error ! where userId=" + riddingUser.getUserId()
							+ " riddingId=" + riddingId);
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.transaction.TransactionService#insertRepostMap(java
	 * .lang.String, com.ridding.meta.WeiBo)
	 */
	@Override
	public long insertRepostMap(WeiBo weiBo, JSONObject jsonObject2) throws TransactionException {
		long nowTime = new Date().getTime();
		RepostMap repostMap = new RepostMap();
		// 用户对象
		JSONObject userObject = jsonObject2.getJSONObject("user");
		long sourceUserId = userObject.getLong("id");
		String sourceUserName = userObject.getString("name");
		repostMap.setRespostSourceId(sourceUserId);
		repostMap.setSourceType(weiBo.getSourceType());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accessUserId", sourceUserId);
		map.put("sourceType", weiBo.getSourceType());
		SourceAccount sourceAccount = sourceAccountMapper.getSourceAccountByAccessUserId(map);
		if (sourceAccount == null) {
			sourceAccount = new SourceAccount();
			sourceAccount.setAccessUserId(sourceUserId);
			sourceAccount.setAccessUserName(sourceUserName);
			sourceAccount.setSourceType(weiBo.getSourceType());
			sourceAccount.setCreateTime(nowTime);
			this.insertSourceAccount(sourceAccount, null);
		}
		repostMap.setRespostUserId(sourceAccount.getUserId());
		repostMap.setCreateTime(nowTime);
		repostMap.setWeiboId(weiBo.getWeiboId());
		// 微博对象
		long repostWeiBoId = jsonObject2.getLong("id");
		repostMap.setRespostWeiBoId(repostWeiBoId);
		if (repostMapWeiBoMapper.addRepostMap(repostMap) < 0) {
			throw new TransactionException("TransactionException insertRepostMap addRepostMap error ! where weiBo.id=" + weiBo.getWeiboId());
		}
		Ridding ridding = riddingMapper.getRidding(weiBo.getRiddingId());
		if (ridding == null) {
			throw new TransactionException("TransactionException insertRepostMap getRiddingMap error ! where weiBo.id=" + weiBo.getWeiboId());
		}
		IMap iMap = iMapMapper.getRiddingMap(ridding.getMapId());
		if (iMap == null) {
			throw new TransactionException("TransactionException insertRepostMap getRiddingMap error ! where weiBo.id=" + weiBo.getWeiboId());
		}
		Ridding newRidding = new Ridding();
		newRidding.setName(ridding.getName());
		newRidding.setLeaderUserId(sourceAccount.getUserId());
		if (!this.insertANewRidding(iMap, newRidding)) {
			throw new TransactionException("TransactionException insertRepostMap insertANewRidding error ! where weiBo.id=" + weiBo.getWeiboId());
		}
		return repostMap.getRespostWeiBoId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingService#deleteRiddingAndLinkedThings(long)
	 */
	public boolean deleteRiddingAndLinkedThings(long riddingId) throws TransactionException {
		if (riddingMapper.deleteRidding(riddingId) < 0) {
			throw new TransactionException("TransactionException deleteRiddingAndLinkedThings deleteRidding error ! where riddingId=" + riddingId);
		}
		if (riddingUserMapper.deleteRiddingUserByRiddingId(riddingId) < 0) {
			throw new TransactionException("TransactionException deleteRiddingAndLinkedThings deleteRiddingUserByRiddingId error ! where riddingId="
					+ riddingId);
		}
		if (riddingPictureMapper.deleteRiddingPicByRiddingId(riddingId) < 0) {
			throw new TransactionException("TransactionException deleteRiddingAndLinkedThings deleteRiddingPicByRiddingId error ! where riddingId="
					+ riddingId);
		}
		if (riddingCommentMapper.deleteRiddingCommentByRiddingId(riddingId) < 0) {
			throw new TransactionException(
					"TransactionException deleteRiddingAndLinkedThings deleteRiddingCommentByRiddingId error ! where riddingId=" + riddingId);
		}
		if (riddingActionMapper.deleteRiddingActionByRiddingId(riddingId) < 0) {
			throw new TransactionException(
					"TransactionException deleteRiddingAndLinkedThings deleteRiddingActionByRiddingId error ! where riddingId=" + riddingId);
		}
		return true;
	}

	/**
	 * 生成taobaoCode算法
	 * 
	 * @param profile
	 */
	private void genCode(Profile profile) {
		StringBuilder sb = new StringBuilder();
		long time = new Date().getTime();
		Random random = new Random(1000000);
		int key = random.nextInt();
		long userId = profile.getUserId();
		String userName = profile.getUserName();

		sb.append(time);
		sb.append("_");
		sb.append(key);
		sb.append("_");
		sb.append(userId);
		sb.append("_");
		sb.append(userName);
		sb.append("_");
		String code = MD5Util.getMD5(sb.toString().getBytes());
		profile.setTaobaoCode(code);
	}
}

package com.ridding.bean.dwr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ridding.constant.RiddingQuitConstant;
import com.ridding.constant.SourceType;
import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.vo.Location;
import com.ridding.meta.vo.ProfileVO;
import com.ridding.security.MyUser;
import com.ridding.service.MapService;
import com.ridding.service.PhotoService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-9 下午08:30:17 Class Description 骑行的dwrbean
 */
@Service("dwrRiddingBean")
public class DwrRiddingBean {

	@Resource
	private TransactionService transactionService;

	@Resource
	private MapService mapService;

	@Resource
	private RiddingService riddingService;

	@Resource
	private ProfileService profileService;

	@Resource
	private PhotoService photoService;


	/**
	 * 删除用户
	 * 
	 * @param riddingId
	 * @param sourceUserId
	 * @param sourceType
	 * @return
	 */
	public boolean deleteUser(long riddingId, long sourceUserId, int sourceType) {
		List<Long> sourceUserIdList = new ArrayList<Long>();
		sourceUserIdList.add(sourceUserId);
		return riddingService.deleteRiddingUsers(sourceUserIdList, riddingId);
	}

	/**
	 * 删除用户通过用户id,使用quit，其实是删除
	 * 
	 * @param riddingId
	 * @param userId
	 * @return
	 */
	public boolean deleteUserByUserId(long riddingId, long userId) {
		return riddingService.quitRidding(userId, riddingId) == RiddingQuitConstant.Success;
	}

	/**
	 * 添加骑行用户
	 * 
	 * @param riddingId
	 * @param profile
	 * @param sourceType
	 * @return
	 */
	public boolean addUser(long riddingId, List<Profile> profileList, int sourceType) {
		return riddingService.insertRiddingUsers(profileList, riddingId, sourceType, -1);
	}

	/**
	 * 通过用户id得到其对应的accessUserId信息
	 * 
	 * @deprecated
	 * @param userIds
	 * @return
	 */
	public List<ProfileVO> getSourceIdList(List<Long> userIds) {
		List<SourceAccount> sourceAccounts = profileService.getSourceAccountByUserIdsSourceType(userIds, SourceType.SINAWEIBO.getValue());
		Map<Long, SourceAccount> map = HashMapMaker.listToMap(sourceAccounts, "getUserId", SourceAccount.class);
		List<ProfileVO> profileVOList = new ArrayList<ProfileVO>(userIds.size());
		for (Long userId : userIds) {
			SourceAccount sourceAccount = map.get(userId);
			if (sourceAccount != null) {
				ProfileVO profileVO = new ProfileVO();
				profileVO.setAccessUserId(sourceAccount.getAccessUserId());
				profileVO.setUserId(userId);
				profileVOList.add(profileVO);
			}
		}
		return profileVOList;
	}

	/**
	 * 更新骑行活动
	 * 
	 * @param locations
	 * @param name
	 * @param beginLocation
	 * @param distance
	 * @param points
	 * @param cityname
	 * @param endLocation
	 * @param midLocations
	 * @return
	 */
	public boolean updateRidding(List<Location> locations, String name, String beginLocation, int distance, String points, String cityname,
			String endLocation, String[] midLocations, long riddingId) {
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
		if (myUser == null) {
			return false;
		}
		Ridding ridding = riddingService.getRidding(riddingId);
		if (ridding == null) {
			return false;
		}
		JSONArray array = new JSONArray();
		if (!ListUtils.isEmptyList(locations)) {
			for (Location location : locations) {
				array.add(location.getLatitude() + "," + location.getLongtitude());
			}
		}
		JSONArray jsonArry = new JSONArray();
		if (!ArrayUtils.isEmpty(midLocations)) {
			for (String midLocation : midLocations) {
				jsonArry.add(midLocation);
			}
		}
		IMap iMap = new IMap();
		iMap.setId(ridding.getMapId());
		iMap.setMidLocation(jsonArry.toString());
		iMap.setMapTaps(array.toString());
		iMap.setBeginLocation(beginLocation);
		iMap.setStatus(IMap.Using);
		iMap.setObjectType(SourceType.Web.getValue());
		iMap.setObjectId(0);
		iMap.setUserId(myUser.getUserId());
		iMap.setDistance(distance);
		JSONArray pointArray = new JSONArray();
		pointArray.add(points);
		iMap.setMapPoint(pointArray.toString());
		iMap.setEndLocation(endLocation);
		iMap.setCityId(mapService.getCityIdByName(cityname));
		iMap.setCreateTime(new Date().getTime());
		mapService.updateRiddingMap(iMap);
		return riddingService.updateRiddingName(riddingId, myUser.getUserId(), name);
	}

	// /**
	// * 创建骑行活动
	// *
	// * @param locations
	// * @return
	// */
	// public long createRidding(List<Location> locations, String name, String
	// beginLocation, int distance, String points, String cityname,
	// String endLocation, String[] midLocations) {
	// MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken)
	// SecurityContextHolder.getContext().getAuthentication()).getDetails();
	// if (myUser == null) {
	// return -1L;
	// }
	// JSONArray array = new JSONArray();
	// if (!ListUtils.isEmptyList(locations)) {
	// for (Location location : locations) {
	// array.add(location.getLatitude() + "," + location.getLongtitude());
	// }
	// }
	// JSONArray jsonArry = new JSONArray();
	// if (!ArrayUtils.isEmpty(midLocations)) {
	// for (String midLocation : midLocations) {
	// jsonArry.add(midLocation);
	// }
	// }
	//
	// IMap iMap = new IMap();
	// iMap.setMidLocation(jsonArry.toString());
	// iMap.setMapTaps(array.toString());
	// iMap.setBeginLocation(beginLocation);
	// iMap.setStatus(IMap.Using);
	// iMap.setObjectType(SourceType.Web.getValue());
	// iMap.setObjectId(0);
	// iMap.setUserId(myUser.getUserId());
	// iMap.setDistance(distance);
	// JSONArray pointArray = new JSONArray();
	// pointArray.add(points);
	// iMap.setMapPoint(pointArray.toString());
	// iMap.setEndLocation(endLocation);
	// iMap.setCityId(mapService.getCityIdByName(cityname));
	// iMap.setCreateTime(new Date().getTime());
	//
	// Ridding ridding = new Ridding();
	// ridding.setLeaderUserId(myUser.getUserId());
	// ridding.setName(name);
	// Photo photo = new Photo();
	// if (photoService.addPhoto(photo) < 0) {
	// return -1;
	// }
	// iMap.setAvatorPic(photo.getId());
	// if (imageUploadService.saveImageFromUrl(iMap.getStaticImgSrc(),
	// photo.getId()) == null) {
	// return -1;
	// }
	// if (transactionService.insertANewRidding(iMap, ridding)) {
	// return ridding.getId();
	// }
	// return -1L;
	// }

}

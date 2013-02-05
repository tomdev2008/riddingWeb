package com.ridding.util.http;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.ridding.constant.SystemConst;
import com.ridding.meta.IMap;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.meta.vo.ProfileVO;
import com.ridding.util.ListUtils;
import com.ridding.util.ObjectUtil;
import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime��2012-3-21 ����02:34:33 Class Description
 */
public class HttpJsonUtil {
	/**
	 * ShowRiddingView
	 * 
	 * @param jsonObject
	 * @param riddingUsers
	 * @return
	 */
	public static void setShowRiddingView(JSONObject jsonObject, List<RiddingUser> riddingUsers) {
		if (ObjectUtil.isEmptyList(riddingUsers)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (RiddingUser riddingUser : riddingUsers) {
			JSONObject object = new JSONObject();
			object.put("userId", riddingUser.getUserId());
			object.put("userid", riddingUser.getUserId());
			object.put("speed", riddingUser.getSpeed());
			object.put("status", riddingUser.getUserStatus());
			object.put("latitude", riddingUser.getLatitude());
			object.put("longtitude", riddingUser.getLongtitude());
			object.put("timebefore", riddingUser.getTimeBefore());
			jsonArray.add(object);
		}
		jsonObject.put("users", jsonArray);
	}

	/**
	 * 插入骑行地图
	 * 
	 * @param jsonObject
	 * @param iMap
	 */
	public static void setRiddingMapOrTaps(JSONObject jsonObject, IMap iMap) {
		if (iMap == null) {
			return;
		}
		if (!StringUtils.isEmpty(iMap.getMapPoint())) {
			jsonObject.put("points", iMap.getMapPoint());
		}
		if (!StringUtils.isEmpty(iMap.getMapUrl()) || !StringUtils.isEmpty(iMap.getMapTaps())) {
			jsonObject.put("mapTaps", iMap.getMapTaps());
		}
		jsonObject.put("distance", iMap.getDistance());
		jsonObject.put("beginLocation", iMap.getBeginLocation());
		jsonObject.put("id", iMap.getId());
	}

	/**
	 * 插入骑行列表
	 * 
	 * @param jsonObject
	 * @param iMap
	 */
	public static void setRiddingList(JSONObject object, List<ActivityRidding> activityRiddings) {
		if (ListUtils.isEmptyList(activityRiddings)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (ActivityRidding activityRidding : activityRiddings) {
			JSONObject jsonObject = new JSONObject();
			if (activityRidding.getRidding() != null) {
				jsonObject.put("name", activityRidding.getRidding().getName());
				jsonObject.put("status", activityRidding.getRidding().getRiddingStatus());
				jsonObject.put("id", activityRidding.getRidding().getId());
				jsonObject.put("distance", activityRidding.getiMap().getDistance());
				jsonObject.put("createtime", activityRidding.getRidding().getCreateTime());
				jsonObject.put("createtimestr", TimeUtil.getFormatTime(activityRidding.getRidding().getCreateTime()));
				jsonObject.put("userCount", activityRidding.getRidding().getUserCount());
			}
			if (activityRidding.getiMap() != null) {
				jsonObject.put("beginLocation", activityRidding.getiMap().getBeginLocation());
				jsonObject.put("endLocation", activityRidding.getiMap().getEndLocation());
				jsonObject.put("mapAvatorPicUrl", SystemConst.returnPhotoUrl(activityRidding.getiMap().getAvatorPicUrl()));
			}

			if (activityRidding.getRiddingUser() != null) {
				jsonObject.put("userRole", activityRidding.getRiddingUser().getUserRole());
			}

			if (activityRidding.getLeaderProfile() != null) {
				jsonObject.put("leaderSAvatorUrl", activityRidding.getLeaderProfile().getsAvatorUrl());
				jsonObject.put("leaderName", activityRidding.getLeaderProfile().getNickName());
				jsonObject.put("leaderUserId", activityRidding.getLeaderProfile().getUserId());
			}
			jsonArray.add(jsonObject);
		}
		object.put("riddinglist", jsonArray);
	}

	/**
	 * 插入用户信息列表
	 * 
	 * @param jsonObject
	 * @param iMap
	 */
	public static void setProfileList(JSONObject object, List<ProfileVO> profileVOs) {
		if (ListUtils.isEmptyList(profileVOs)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (ProfileVO profileVO : profileVOs) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userid", profileVO.getUserId());
			jsonObject.put("userRole", profileVO.getUserRole());
			jsonObject.put("userrole", profileVO.getUserRole());
			jsonObject.put("nickname", profileVO.getNickName());
			jsonObject.put("bavatorUrl", profileVO.getbAvatorUrl());
			jsonObject.put("savatorUrl", profileVO.getsAvatorUrl());
			jsonObject.put("bavatorurl", profileVO.getbAvatorUrl());
			jsonObject.put("savatorurl", profileVO.getsAvatorUrl());
			jsonArray.add(jsonObject);
		}
		object.put("userlist", jsonArray);
		System.out.println(object.toString());
	}

	/**
	 * 插入sourceAccount
	 * 
	 * @param object
	 * @param sourceAccounts
	 */
	public static void setSourceAccount(JSONObject object, List<SourceAccount> sourceAccounts) {
		if (ListUtils.isEmptyList(sourceAccounts)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (SourceAccount sourceAccount : sourceAccounts) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("accessuserid", sourceAccount.getAccessUserId());
			jsonObject.put("userid", sourceAccount.getUserId());
			jsonArray.add(jsonObject);
		}
		object.put("useridaccessuseridlist", jsonArray);
	}

	/**
	 * 插入返回骑行图片的信息
	 * 
	 * @param object
	 * @param riddingPictures
	 */
	public static void setupLoadedRiddingPicture(JSONObject object, List<RiddingPicture> riddingPictures) {
		if (ListUtils.isEmptyList(riddingPictures)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (RiddingPicture riddingPicture : riddingPictures) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("photourl", SystemConst.getValue("IMAGEHOST") + riddingPicture.getPhotoUrl());
			jsonObject.put("latitude", riddingPicture.getLatitude());
			jsonObject.put("longtitude", riddingPicture.getLongtitude());
			jsonObject.put("takepiclocation", riddingPicture.getTakePicLocation());
			jsonObject.put("createtime", riddingPicture.getCreateTime());
			jsonObject.put("takepicdate", riddingPicture.getTakePicDate());
			jsonObject.put("description", riddingPicture.getDescription());
			jsonObject.put("width", riddingPicture.getWidth());
			jsonObject.put("height", riddingPicture.getHeight());
			jsonObject.put("savatorurl", riddingPicture.getsAvatorUrl());
			jsonArray.add(jsonObject);
		}
		object.put("riddingpictures", jsonArray);
	}

	/**
	 * 插入骑行信息
	 * 
	 * @param object
	 * @param riddingPictures
	 */
	public static void setRiddingByLastUpdateTime(JSONObject object, List<Ridding> riddingList) {
		JSONArray jsonArray = new JSONArray();
		if (!ListUtils.isEmptyList(riddingList)) {
			for (Ridding ridding : riddingList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", ridding.getName());
				jsonObject.put("status", ridding.getRiddingStatus());
				jsonObject.put("id", ridding.getId());
				jsonObject.put("distance", ridding.getDistance());
				jsonObject.put("createtimestr", TimeUtil.getFormatTime(ridding.getCreateTime()));
				jsonObject.put("createtime", ridding.getCreateTime());
				// 老版本
				jsonObject.put("lastUpdateTime", TimeUtil.getFormatTime(ridding.getLastUpdateTime()));
				jsonObject.put("leaderUserId", ridding.getLeaderUserId());
				// 新版本
				jsonObject.put("lastupdatetime", TimeUtil.getFormatTime(ridding.getLastUpdateTime()));
				jsonObject.put("leaderuserid", ridding.getLeaderUserId());
				jsonObject.put("firstpicurl", ridding.getFirstPicUrl());
				jsonObject.put("weight", ridding.getaPublic().getWeight());
				jsonArray.add(jsonObject);
			}
		}
		object.put("riddingpubliclist", jsonArray);
	}
}

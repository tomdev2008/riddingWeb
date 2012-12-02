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
import com.ridding.meta.Ridding.RiddingStatus;
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
	public static void setRiddingList(JSONObject object, List<RiddingUser> riddingUserList) {
		if (ListUtils.isEmptyList(riddingUserList)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (RiddingUser riddingUser : riddingUserList) {
			if (riddingUser.getStatus() != RiddingStatus.Deleted.getValue()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", riddingUser.getSelfName());
				jsonObject.put("status", riddingUser.getStatus());
				jsonObject.put("id", riddingUser.getRiddingId());
				jsonObject.put("distance", riddingUser.getDistance());
				jsonObject.put("createtime", riddingUser.getCreateTime());
				jsonObject.put("createtimestr", TimeUtil.getFormatTime(riddingUser.getCreateTime()));
				jsonObject.put("beginLocation", riddingUser.getBeginLocation());
				jsonObject.put("endLocation", riddingUser.getEndLocation());
				jsonObject.put("userRole", riddingUser.getUserRole());
				jsonObject.put("mapAvatorPicUrl", riddingUser.getAvatorPicUrl());
				jsonObject.put("userCount", riddingUser.getUserCount());
				if (riddingUser.getLeaderProfile() != null) {
					jsonObject.put("leaderSAvatorUrl", riddingUser.getLeaderProfile().getsAvatorUrl());
					jsonObject.put("leaderName", riddingUser.getLeaderProfile().getNickName());
					jsonObject.put("leaderUserId", riddingUser.getLeaderProfile().getUserId());
				}
				jsonArray.add(jsonObject);
			}
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
			jsonObject.put("nickname", profileVO.getNickName());
			jsonObject.put("bavatorUrl", profileVO.getbAvatorUrl());
			jsonObject.put("savatorUrl", profileVO.getsAvatorUrl());
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
			jsonObject.put("fileName", riddingPicture.getLocalName());
			jsonObject.put("photoUrl", SystemConst.getValue("IMAGEHOST") + riddingPicture.getPhotoUrl());
			jsonObject.put("latitude", riddingPicture.getLatitude());
			jsonObject.put("longtitude", riddingPicture.getLongtitude());
			jsonObject.put("takepiclocation", riddingPicture.getTakePicLocation());
			jsonObject.put("takepicdate", riddingPicture.getTakePicDate());
			jsonObject.put("description", riddingPicture.getDescription());
			jsonObject.put("width", riddingPicture.getWidth());
			jsonObject.put("height", riddingPicture.getHeight());
			jsonObject.put("savatorurl", riddingPicture.getsAvatorUrl());
			jsonArray.add(jsonObject);
		}
		object.put("riddingPictures", jsonArray);
	}

	/**
	 * 插入骑行信息
	 * 
	 * @param object
	 * @param riddingPictures
	 */
	public static void setRiddingByLastUpdateTime(JSONObject object, List<Ridding> riddingList) {
		if (ListUtils.isEmptyList(riddingList)) {
			return;
		}
		JSONArray jsonArray = new JSONArray();
		for (Ridding ridding : riddingList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", ridding.getName());
			jsonObject.put("status", ridding.getRiddingStatus());
			jsonObject.put("id", ridding.getId());
			jsonObject.put("distance", ridding.getDistance());
			jsonObject.put("createtime", TimeUtil.getFormatTime(ridding.getCreateTime()));
			jsonObject.put("lastUpdateTime", TimeUtil.getFormatTime(ridding.getLastUpdateTime()));
			jsonObject.put("leaderUserId", ridding.getLeaderUserId());
			jsonObject.put("firstpicurl", ridding.getFirstPicUrl());
			jsonArray.add(jsonObject);
		}
		object.put("riddingpubliclist", jsonArray);
	}
}

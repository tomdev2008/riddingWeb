package com.ridding.util.http;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ridding.constant.SystemConst;
import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingComment;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.Ridding.RiddingStatus;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.meta.vo.ProfileVO;
import com.ridding.util.ListUtils;
import com.ridding.util.ObjectUtil;
import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-7 下午10:02:19 Class Description
 */
public class HttpServletUtil2 {
	/**
	 * 
	 * @param jsonObject
	 * @param name
	 * @return
	 */
	private static void returnDataObject(JSONObject jsonObject, String name, JSONObject returnObject) {
		returnObject.put(name, jsonObject);
	}

	/**
	 * 
	 * @param profile
	 * @param sourceAccount
	 */
	public static JSONObject parseGetUserProfile(Profile profile, SourceAccount sourceAccount, int riddingCount) {
		if (profile == null || sourceAccount == null || riddingCount < 0) {
			return new JSONObject();
		}
		JSONObject userObject = new JSONObject();
		userObject.put("userid", profile.getUserId());
		userObject.put("username", profile.getUserName());
		userObject.put("nickname", profile.getNickName());
		userObject.put("bavatorurl", profile.getbAvatorUrl());
		userObject.put("savatorurl", profile.getsAvatorUrl());
		userObject.put("totaldistance", profile.getTotalDistance());
		userObject.put("sourceid", sourceAccount.getAccessUserId());
		userObject.put("accesstoken", sourceAccount.getAccessToken());
		userObject.put("riddingcount", riddingCount);
		JSONObject returnObject = new JSONObject();
		HttpServletUtil2.returnDataObject(userObject, "user", returnObject);
		return returnObject;
	}

	/**
	 * 
	 * @param riddingUserList
	 * @return
	 */
	public static JSONArray parseGetRiddingList(List<ActivityRidding> activityRiddings) {
		if (ListUtils.isEmptyList(activityRiddings)) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		for (ActivityRidding activityRidding : activityRiddings) {
			JSONObject riddingObject = new JSONObject();
			if(activityRidding.getRidding()!=null){
				riddingObject.put("riddingid", activityRidding.getRidding().getId());
				riddingObject.put("riddingstatus", activityRidding.getRidding().getRiddingStatus());
				riddingObject.put("riddingname", activityRidding.getRidding().getName());
				riddingObject.put("createtime", activityRidding.getRidding().getCreateTime());
				riddingObject.put("createtimestr", TimeUtil.getFormatTime(activityRidding.getRidding().getCreateTime()));
				riddingObject.put("usercount", activityRidding.getRidding().getUserCount());
				riddingObject.put("carecount", activityRidding.getRidding().getCareCount());
				riddingObject.put("commentcount", activityRidding.getRidding().getCommentCount());
				riddingObject.put("usecount", activityRidding.getRidding().getUseCount());
				riddingObject.put("likecount", activityRidding.getRidding().getLikeCount());
			}
			if(activityRidding.getRiddingUser()!=null){
				riddingObject.put("userrole", activityRidding.getRiddingUser().getUserRole());
			}
	
			if(activityRidding.getiMap()!=null){
				JSONObject mapObject = new JSONObject();
				mapObject.put("distance", activityRidding.getiMap().getDistance());
				mapObject.put("beginlocation", activityRidding.getiMap().getBeginLocation());
				mapObject.put("endlocation", activityRidding.getiMap().getEndLocation());
				mapObject.put("avatorpicurl", SystemConst.returnPhotoUrl(activityRidding.getiMap().getAvatorPicUrl()));

				riddingObject.put("map", mapObject);
			}
			
			

			if (activityRidding.getLeaderProfile() != null) {
				JSONObject userObject = new JSONObject();
				userObject.put("savatorurl", activityRidding.getLeaderProfile().getsAvatorUrl());
				userObject.put("username", activityRidding.getLeaderProfile().getNickName());
				userObject.put("userid", activityRidding.getLeaderProfile().getUserId());
				riddingObject.put("user", userObject);
			}
			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(riddingObject, "ridding", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}

	/**
	 * 
	 * @param iMap
	 * @return
	 */
	public static JSONObject parseGetRidingMapOrLocation(IMap iMap) {
		if (iMap == null) {
			return new JSONObject();
		}
		JSONObject mapObject = new JSONObject();
		if (!StringUtils.isEmpty(iMap.getMapPoint())) {
			mapObject.put("mappoint", iMap.getMapPoint());
		}
		if (!StringUtils.isEmpty(iMap.getMapUrl()) || !StringUtils.isEmpty(iMap.getMapTaps())) {
			mapObject.put("maptaps", iMap.getMapTaps());
		}
		mapObject.put("distance", iMap.getDistance());
		mapObject.put("mapid", iMap.getId());
		mapObject.put("beginlocation", iMap.getBeginLocation());
		JSONObject returnObject = new JSONObject();
		HttpServletUtil2.returnDataObject(mapObject, "map", returnObject);
		return returnObject;
	}

	/**
	 * 
	 * @param mapFix
	 * @param latitude
	 * @param longtitude
	 * @return
	 */
	public static JSONObject parseGetFixedCoordinate(MapFix mapFix, double latitude, double longtitude) {
		if (mapFix == null) {
			return new JSONObject();
		}
		JSONObject mapFixObject = new JSONObject();
		mapFixObject.put("latitude", latitude);
		mapFixObject.put("longtitude", longtitude);
		mapFixObject.put("reallatitude", mapFix.getLatitude());
		mapFixObject.put("reallongtitude", mapFix.getLongtitude());
		JSONObject returnObject = new JSONObject();
		HttpServletUtil2.returnDataObject(mapFixObject, "mapfix", returnObject);
		return returnObject;
	}

	/**
	 * 
	 * @param profileVOs
	 * @return
	 */
	public static JSONArray parseGetRiddingUserList(List<ProfileVO> profileVOs) {
		if (ListUtils.isEmptyList(profileVOs)) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		for (ProfileVO profileVO : profileVOs) {
			JSONObject userObject = new JSONObject();
			userObject.put("userid", profileVO.getUserId());
			userObject.put("userrole", profileVO.getUserRole());
			userObject.put("username", profileVO.getNickName());
			userObject.put("bavatorurl", profileVO.getbAvatorUrl());
			userObject.put("savatorurl", profileVO.getsAvatorUrl());
			if (profileVO.getSourceAccount() != null) {
				userObject.put("sourceid", profileVO.getSourceAccount().getAccessUserId());
			}
			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(userObject, "user", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}

	/**
	 * 
	 * @param riddingPictures
	 * @return
	 */
	public static JSONArray parseGetuploadedPhotos(List<RiddingPicture> riddingPictures) {
		if (ListUtils.isEmptyList(riddingPictures)) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		for (RiddingPicture riddingPicture : riddingPictures) {
			JSONObject pictureObject = new JSONObject();
			pictureObject.put("filename", riddingPicture.getLocalName());
			pictureObject.put("photourl", SystemConst.getValue("IMAGEHOST") + riddingPicture.getPhotoUrl());
			pictureObject.put("latitude", riddingPicture.getLatitude());
			pictureObject.put("longtitude", riddingPicture.getLongtitude());
			pictureObject.put("location", riddingPicture.getTakePicLocation());
			pictureObject.put("createtime", riddingPicture.getCreateTime());
			pictureObject.put("createtimestr", TimeUtil.getFormatTime(riddingPicture.getCreateTime()));
			pictureObject.put("takepicdatel", riddingPicture.getTakePicDate());
			pictureObject.put("takepicdatestr", TimeUtil.getFormatTime(riddingPicture.getTakePicDate()));
			pictureObject.put("description", riddingPicture.getDescription());
			pictureObject.put("width", riddingPicture.getWidth());
			pictureObject.put("height", riddingPicture.getHeight());
			JSONObject userObject = new JSONObject();
			userObject.put("savatorurl", riddingPicture.getsAvatorUrl());
			pictureObject.put("user", userObject);
			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(pictureObject, "picture", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}

	public static JSONArray parseGetGoingRiddings(List<Ridding> riddingList) {
		if (ListUtils.isEmptyList(riddingList)) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		for (Ridding ridding : riddingList) {
			JSONObject activityObject = new JSONObject();
			activityObject.put("weight", ridding.getWeight());
			activityObject.put("firstpicurl", SystemConst.returnPhotoUrl(ridding.getFirstPicUrl()));

			JSONObject riddingObject = new JSONObject();
			riddingObject.put("riddingid", ridding.getId());
			riddingObject.put("riddingname", ridding.getName());
			riddingObject.put("riddingstatus", ridding.getRiddingStatus());
			riddingObject.put("createtime", ridding.getCreateTime());
			riddingObject.put("createtimestr", TimeUtil.getFormatTime(ridding.getCreateTime()));
			riddingObject.put("lastupdatetime", ridding.getLastUpdateTime());
			riddingObject.put("lastupdatetimestr", TimeUtil.getFormatTime(ridding.getLastUpdateTime()));
			riddingObject.put("usercount", ridding.getUserCount());
			riddingObject.put("carecount", ridding.getCareCount());
			riddingObject.put("commentcount", ridding.getCommentCount());
			riddingObject.put("usecount", ridding.getUseCount());
			riddingObject.put("likecount", ridding.getLikeCount());

			JSONObject leaderUserObject = new JSONObject();
			leaderUserObject.put("userid", ridding.getLeaderUserId());
			riddingObject.put("user", leaderUserObject);

			JSONObject mapObject = new JSONObject();
			mapObject.put("distance", ridding.getDistance());
			riddingObject.put("map", mapObject);

			activityObject.put("ridding", riddingObject);

			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(activityObject, "activity", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}

	/**
	 * 
	 * @param riddingUsers
	 * @return
	 */
	public static JSONArray parseShowRiddingView(List<RiddingUser> riddingUsers) {
		if (ObjectUtil.isEmptyList(riddingUsers)) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		for (RiddingUser riddingUser : riddingUsers) {
			JSONObject userObject = new JSONObject();
			userObject.put("userid", riddingUser.getUserId());
			userObject.put("speed", riddingUser.getSpeed());
			userObject.put("status", riddingUser.getUserStatus());
			userObject.put("latitude", riddingUser.getLatitude());
			userObject.put("longtitude", riddingUser.getLongtitude());
			userObject.put("timebefore", riddingUser.getTimeBefore());
			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(userObject, "user", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}

	/**
	 * 
	 * @param sourceAccounts
	 * @return
	 */
	public static JSONArray parseGetUserPublicMessage(List<SourceAccount> sourceAccounts) {
		if (ListUtils.isEmptyList(sourceAccounts)) {
			return null;
		}
		JSONArray jsonArray = new JSONArray();
		for (SourceAccount sourceAccount : sourceAccounts) {
			JSONObject userObject = new JSONObject();
			userObject.put("sourceid", sourceAccount.getAccessUserId());
			userObject.put("userid", sourceAccount.getUserId());
			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(userObject, "user", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}

	public static JSONObject parseAddRiddingMap(Ridding ridding, String url) {
		if (ridding == null) {
			return null;
		}
		JSONObject returnObject = new JSONObject();

		JSONObject riddingObject = new JSONObject();
		riddingObject.put("riddingid", ridding.getId());

		JSONObject mapObject = new JSONObject();
		mapObject.put("avatorpicurl", url);

		riddingObject.put("map", mapObject);

		HttpServletUtil2.returnDataObject(riddingObject, "ridding", returnObject);

		return returnObject;
	}

	public static JSONObject parseAddRiddingComment(RiddingComment riddingComment) {
		if (riddingComment == null) {
			return null;
		}
		JSONObject returnObject = new JSONObject();

		JSONObject commentObject = new JSONObject();
		commentObject.put("riddingid", riddingComment.getRiddingId());
		commentObject.put("id", riddingComment.getId());
		commentObject.put("userid", riddingComment.getUserId());
		commentObject.put("touserid", riddingComment.getToUserId());
		commentObject.put("text", riddingComment.getText());
		commentObject.put("usepicurl", riddingComment.getUsePicUrl());
		commentObject.put("commenttype", riddingComment.getCommentType());
		commentObject.put("replyId", riddingComment.getReplyId());
		commentObject.put("createtime", riddingComment.getCreateTime());
		commentObject.put("createtimestr", TimeUtil.getFormatTime(riddingComment.getCreateTime()));
		commentObject.put("beforetime", TimeUtil.getTimeago(riddingComment.getCreateTime(), true));
		HttpServletUtil2.returnDataObject(commentObject, "comment", returnObject);

		return returnObject;
	}

	public static JSONArray parseRiddingComment(List<RiddingComment> riddingComments) {
		if (ObjectUtil.isEmptyList(riddingComments)) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		for (RiddingComment riddingComment : riddingComments) {
			JSONObject commentObject = new JSONObject();
			commentObject.put("riddingid", riddingComment.getRiddingId());
			commentObject.put("id", riddingComment.getId());
			commentObject.put("userid", riddingComment.getUserId());
			commentObject.put("touserid", riddingComment.getToUserId());
			commentObject.put("text", riddingComment.getText());
			commentObject.put("usepicurl", riddingComment.getUsePicUrl());
			commentObject.put("commenttype", riddingComment.getCommentType());
			commentObject.put("replyId", riddingComment.getReplyId());
			commentObject.put("createtime", riddingComment.getCreateTime());
			commentObject.put("createtimestr", TimeUtil.getFormatTime(riddingComment.getCreateTime()));
			commentObject.put("beforetime", TimeUtil.getTimeago(riddingComment.getCreateTime(), true));
			if (riddingComment.getUserProfile() != null) {
				JSONObject userObject = new JSONObject();
				userObject.put("userid", riddingComment.getUserProfile().getUserId());
				userObject.put("savatorurl", riddingComment.getUserProfile().getsAvatorUrl());
				userObject.put("username", riddingComment.getUserProfile().getUserName());
				userObject.put("nickname", riddingComment.getUserProfile().getNickName());
				userObject.put("bavatorurl", riddingComment.getUserProfile().getbAvatorUrl());
				userObject.put("savatorurl", riddingComment.getUserProfile().getsAvatorUrl());
				commentObject.put("user", userObject);
			}

			if (riddingComment.getToUserProfile() != null) {
				JSONObject touserObject = new JSONObject();
				touserObject.put("userid", riddingComment.getToUserProfile().getUserId());
				touserObject.put("savatorurl", riddingComment.getToUserProfile().getsAvatorUrl());
				touserObject.put("username", riddingComment.getToUserProfile().getUserName());
				touserObject.put("nickname", riddingComment.getToUserProfile().getNickName());
				touserObject.put("bavatorurl", riddingComment.getToUserProfile().getbAvatorUrl());
				touserObject.put("savatorurl", riddingComment.getToUserProfile().getsAvatorUrl());
				commentObject.put("touser", touserObject);
			}

			JSONObject returnObject = new JSONObject();
			HttpServletUtil2.returnDataObject(commentObject, "comment", returnObject);
			jsonArray.add(returnObject);
		}
		return jsonArray;
	}
}

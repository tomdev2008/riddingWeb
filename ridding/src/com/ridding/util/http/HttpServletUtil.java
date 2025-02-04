package com.ridding.util.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ridding.exception.RequestBodyIsNullException;
import com.ridding.meta.Account;
import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;
import com.ridding.meta.Profile;
import com.ridding.meta.Public;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingComment;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.UserRelation;
import com.ridding.meta.vo.ProfileSourceFeed;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-5 02:04:03 Class Description httputil
 */
public final class HttpServletUtil {
	private static Logger logger = Logger.getLogger(HttpServletUtil.class);
	/**
	 * xml的最大长度
	 */
	public static final int MAX_LENGTH_XML = 1024;

	/**
	 * 解析请求
	 * 
	 * @param request
	 * @param encoding
	 * @return
	 */
	public static String parseRequestAsString(HttpServletRequest request, String encoding) {
		InputStream in = null;
		BufferedInputStream bufInput = null;
		ByteArrayOutputStream out = null;
		try {
			in = request.getInputStream();
			bufInput = new BufferedInputStream(in);
			out = new ByteArrayOutputStream();
			byte[] b = new byte[MAX_LENGTH_XML];
			int c = 0;
			while ((c = bufInput.read(b)) != -1) {
				out.write(b, 0, c);
			}
			return (encoding == null) ? out.toString() : out.toString(encoding);
		} catch (IOException e) {
			logger.error("HttpServletUtil parseRequestAsString io error!");
			return null;
		} finally {
			IOUtils.closeQuietly(bufInput);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 解析登陆后的json信息
	 * 
	 * @param xml
	 * @return
	 */
	public static Account parseToAccount4login(String jsonString) throws Exception {
		// JSONObject jsonObject = JSONObject.fromObject(jsonString);
		// if (jsonObject == null) {
		// throw new RequestBodyIsNullException();
		// }
		// Account account = new Account();
		// try {
		// if (jsonObject.getLong("accessuserid")) {
		// return null;
		// }
		//			
		// } catch (Exception e) {
		// throw new RequestBodyIsNullException();
		// }
		// return account;
		return null;
	}

	/**
	 * 解析登陆的用户信息
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static Profile parseToProfile4login(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		Profile profile = new Profile();
		try {
			profile.setsAvatorUrl(jsonObject.getString("savatorurl"));
			profile.setbAvatorUrl(jsonObject.getString("bavatorurl"));
			profile.setUserName(jsonObject.getString("accessusername"));
		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return profile;
	}

	/**
	 * 解析用户的骑行信息
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static RiddingUser parseToRidding4RiddingView(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		RiddingUser riddingUser = new RiddingUser();
		try {
			riddingUser.setLatitude(jsonObject.getDouble("latitude"));
			riddingUser.setLongtitude(jsonObject.getDouble("longtitude"));
			riddingUser.setUserStatus(jsonObject.getInt("status"));
			riddingUser.setSpeed(jsonObject.getDouble("speed"));
			riddingUser.setShowTeamer(jsonObject.getInt("showTeamer") == 1 ? true : false);
		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}

		return riddingUser;
	}

	/**
	 * 解析imap的maplocation
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static IMap parseTosetRidingMapLocation(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		IMap iMap = new IMap();
		try {
			iMap.setMapPoint(jsonObject.getString("points"));
			iMap.setMapPoint(iMap.getMapPoint().substring(1, iMap.getMapPoint().length() - 1));
			iMap.setDistance(jsonObject.getInt("distance"));
			iMap.setId(jsonObject.getLong("mapid"));
			iMap.setBeginLocation(jsonObject.getString("beginlocation"));
		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return iMap;
	}

	/**
	 * parse出骑行
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static Ridding parseToRidding(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		Ridding ridding = new Ridding();
		try {
			long time = jsonObject.getLong("createtime");
			if (time <= 0) {
				ridding.setCreateTime(new Date().getTime());
			} else {
				ridding.setCreateTime(time);
			}
			ridding.setLimit(jsonObject.getInt("limit"));
			if (jsonObject.get("larger") != null) {// 兼容第一个版本
				ridding.setLarger(jsonObject.getInt("larger") == 1 ? true : false);
			} else {
				ridding.setLarger(false);
			}
		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return ridding;
	}

	/**
	 * 生成新浪微博id列表
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static List<Profile> parseToAddAccount(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		try {
			String arrayString = jsonObject.get("addids").toString();
			arrayString = arrayString.substring(1, arrayString.length() - 1);
			JSONArray array = JSONArray.fromObject(arrayString);
			List<Profile> profileList = new ArrayList<Profile>(array.size());
			for (Object object : array.toArray()) {
				JSONObject userObject = JSONObject.fromObject(object);
				Profile profile = new Profile();
				profile.setAccessUserId(userObject.getLong("accessuserid"));
				profile.setUserName(userObject.getString("nickname"));
				profile.setNickName(userObject.getString("nickname"));
				profileList.add(profile);
			}
			return profileList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequestBodyIsNullException();
		}
	}

	/**
	 * 删除
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static List<Long> parseToDeleteAccount(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		try {
			String objectString = "deleteids";
			Object aObject = jsonObject.get(objectString);
			if (aObject == null) {
				return null;
			}
			String arrayString = aObject.toString();
			arrayString = arrayString.substring(1, arrayString.length() - 1);
			JSONArray array = JSONArray.fromObject(arrayString);
			List<Long> idList = new ArrayList<Long>(array.size());
			for (Object object : array.toArray()) {
				Long id = Long.valueOf(object.toString());
				idList.add(id);
			}
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequestBodyIsNullException();
		}
	}

	public static ProfileSourceFeed parseToProfileSourceFeed(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		ProfileSourceFeed profileSourceFeed = new ProfileSourceFeed();
		try {
			profileSourceFeed.setSourceType(jsonObject.getInt("sourcetype"));
			List<Long> userIdList = new ArrayList<Long>();
			String arrayString = jsonObject.get("userids").toString();
			arrayString = arrayString.substring(1, arrayString.length() - 1);
			JSONArray jsonArray = JSONArray.fromObject(arrayString);
			for (Object object : jsonArray.toArray()) {
				userIdList.add(Long.valueOf(object.toString()));
			}
			profileSourceFeed.setUserIdList(userIdList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RequestBodyIsNullException();
		}
		return profileSourceFeed;
	}

	public static RiddingPicture parseToRiddingPicture(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		RiddingPicture riddingPicture = new RiddingPicture();
		try {
			riddingPicture.setPhotoUrl(jsonObject.getString("filekey"));
			riddingPicture.setLatitude(jsonObject.getDouble("latitude"));
			riddingPicture.setLongtitude(jsonObject.getDouble("longtitude"));
			riddingPicture.setTakePicDate(jsonObject.getLong("takepicdate"));
			riddingPicture.setTakePicLocation(jsonObject.getString("takepiclocation"));
			riddingPicture.setDescription(jsonObject.getString("description"));
			riddingPicture.setWidth(jsonObject.getInt("width"));
			riddingPicture.setHeight(jsonObject.getInt("height"));
			long nowTime = new Date().getTime();
			riddingPicture.setCreateTime(nowTime);
			riddingPicture.setLastUpdateTime(nowTime);
			riddingPicture.setStatus(0);
		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return riddingPicture;
	}

	/**
	 * 从地图转
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static IMap parseFromMap(String jsonString, Ridding ridding) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			logger.error("JSONObject.fromObject error where json=" + jsonString);
			throw new RequestBodyIsNullException();
		}
		IMap iMap = new IMap();
		try {
			String points = jsonObject.getString("points");
			if (!StringUtils.isEmpty(points)) {
				points = points.substring(1, points.length() - 1);
			}
			iMap.setMapPoint(points);
			String mapTaps = jsonObject.get("mapTaps").toString();
			if (!StringUtils.isEmpty(mapTaps)) {
				mapTaps = mapTaps.substring(1, mapTaps.length() - 1);
			}
			iMap.setMapTaps(mapTaps);

			String midLocations = jsonObject.getString("midlocations");
			if (!StringUtils.isEmpty(midLocations)) {
				midLocations = midLocations.substring(1, midLocations.length() - 1);
			}
			iMap.setMidLocation(midLocations);

			String beginLocation = jsonObject.getString("beginlocation");
			if (beginLocation.length() > 20) {
				beginLocation = beginLocation.substring(0, 20);
			}
			iMap.setBeginLocation(beginLocation);
			String endLocation = jsonObject.getString("endlocation");
			if (endLocation.length() > 20) {
				endLocation = endLocation.substring(0, 20);
			}
			iMap.setEndLocation(endLocation);
			iMap.setDistance(jsonObject.getInt("distance"));
			ridding.setName(jsonObject.getString("riddingname"));
			if (jsonObject.get("urlkey") != null) {
				iMap.setAvatorPicUrl(jsonObject.getString("urlkey"));
			}
			if (jsonObject.get("cityname") != null) {
				iMap.setCityName(jsonObject.getString("cityname"));
			}
			if (jsonObject.get("ispublic") != null) {
				if (jsonObject.getInt("ispublic") > 0) {
					ridding.isPublic = 1;
				}
			}
			if (jsonObject.get("issyncsina") != null) {
				ridding.setIsSyncSina(jsonObject.getInt("issyncsina"));
			}
			if (jsonObject.get("syncwifi") != null) {
				ridding.setUserSyncWifi(jsonObject.getInt("syncwifi"));
			} else {
				ridding.setUserSyncWifi(RiddingUser.no);
			}
			if (jsonObject.get("riddingtype") != null) {
				ridding.setRiddingType(jsonObject.getInt("riddingtype"));
			}

		} catch (Exception e) {
			logger.error("info=" + jsonString);
			throw new RequestBodyIsNullException();

		}
		return iMap;
	}

	public static Ridding parseToRiddingByLastUpdateTime(String jsonString) throws Exception {
		if (jsonString.isEmpty()) {
			return null;
		}
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		Ridding ridding = new Ridding();
		Public public1 = new Public();
		ridding.setaPublic(public1);
		try {
			// 0表示进行中,1表示推荐
			ridding.setIsRecom(jsonObject.getInt("type"));
			if (ridding.getIsRecom() == 1) {
				int weight = jsonObject.getInt("weight");
				if (weight < 0) {
					ridding.getaPublic().setWeight(9999);
				} else {
					ridding.getaPublic().setWeight(weight);
				}
			} else {
				long time = jsonObject.getLong("lastupdatetime");
				if (time < 0) {
					ridding.setLastUpdateTime(new Date().getTime());
				} else {
					ridding.setLastUpdateTime(time);
				}
			}
			ridding.setLimit(jsonObject.getInt("limit"));
			ridding.setLarger(jsonObject.getInt("larger") == 1 ? true : false);

		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return ridding;
	}

	/**
	 * 添加评论
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static RiddingComment parseToRiddingComment(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		RiddingComment riddingComment = new RiddingComment();
		try {
			riddingComment.setText(jsonObject.getString("text"));
			riddingComment.setToUserId(jsonObject.getLong("touserid"));
			riddingComment.setUsePicUrl(jsonObject.getString("usepicurl"));
			riddingComment.setCommentType(jsonObject.getInt("commenttype"));
			riddingComment.setReplyId(jsonObject.getLong("replyid"));
			long nowTime = new Date().getTime();
			riddingComment.setCreateTime(nowTime);
			riddingComment.setLastUpdateTime(nowTime);
		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return riddingComment;
	}

	/**
	 * 得到评论
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static RiddingComment parseToCommentByLastCreateTime(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		RiddingComment riddingComment = new RiddingComment();
		try {
			long time = jsonObject.getLong("lastcreatetime");
			if (time < 0) {
				riddingComment.setLastCreateTime(new Date().getTime());
			} else {
				riddingComment.setLastCreateTime(time);
			}
			riddingComment.setLimit(jsonObject.getInt("limit"));
			riddingComment.setLarger(jsonObject.getInt("larger") == 1 ? true : false);

		} catch (Exception e) {
			throw new RequestBodyIsNullException();
		}
		return riddingComment;
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static Profile parseUpdateBackground(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		Profile profile = new Profile();
		if (jsonObject.get("url") != null) {
			profile.setBackgroundUrl(jsonObject.getString("url"));
		}
		return profile;
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static UserRelation parseRemoveOrAddUserRelation(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}
		UserRelation userRelation = new UserRelation();
		userRelation.setUserId(jsonObject.getLong("userid"));
		userRelation.setToUserId(jsonObject.getLong("touserid"));
		userRelation.setStatus(jsonObject.getInt("status"));
		return userRelation;
	}

	/**
	 * 
	 * @auther zyslovely@gmail.com
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static List<MapFix> parseMapFixs(String jsonString) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		if (jsonObject == null) {
			throw new RequestBodyIsNullException();
		}

		JSONArray array = jsonObject.getJSONArray("mapfixs");
		List<MapFix> mapFixs = new ArrayList<MapFix>(array.size());
		for (Object object : array.toArray()) {
			JSONObject jObject = JSONObject.fromObject(object);
			MapFix mapFix = new MapFix();
			mapFix.setLatitude(jObject.getDouble("latitude"));
			mapFix.setLongtitude(jObject.getDouble("longitude"));
			mapFixs.add(mapFix);
		}
		return mapFixs;
	}
}

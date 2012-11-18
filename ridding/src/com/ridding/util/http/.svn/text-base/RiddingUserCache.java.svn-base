package com.ridding.util.http;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ridding.meta.RiddingUser;
import com.ridding.meta.vo.RiddingUserVO;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 01:30:57 Class Description 骑行用户经纬度信息缓存
 */
public final class RiddingUserCache {
	/**
	 * 大小
	 */
	private static int size = 64;
	/**
	 * 骑行用户map
	 */
	private static Map<Long, RiddingUserVO> riddingUsersMap = new ConcurrentHashMap<Long, RiddingUserVO>(size);

	/**
	 * 创建缓存key
	 * 
	 * @param userId
	 * @param parameters
	 * @return
	 */
	public static String createUserKey(Object... parameters) {
		StringBuilder key = new StringBuilder();
		if (parameters == null) {
			return null;
		}
		for (Object object : parameters) {
			key.append(object.toString() + "_");
		}
		return key.toString();
	}

	/**
	 * 得到缓存中的骑行用户
	 * 
	 * @param riddingKey
	 * @param userKey
	 * @return
	 */
	public static RiddingUser get(long riddingKey, String userKey) {
		RiddingUserVO riddingUserVO = riddingUsersMap.get(riddingKey);
		if (riddingUserVO == null) {
			return null;
		}
		long time = new Date().getTime();
		RiddingUser riddingUser = riddingUserVO.getMap().get(userKey);
		riddingUserVO.setSaveCacheTime(time);
		return riddingUser;
	}

	/**
	 *插入骑行用户
	 * 
	 * @param key
	 * @param riddingUser
	 */
	public static void set(long riddingKey, String userKey, RiddingUser riddingUser) {
		RiddingUserVO riddingUserVO = riddingUsersMap.get(riddingKey);
		ConcurrentHashMap<String, RiddingUser> riddingMap = null;
		if (riddingUserVO == null) {
			riddingUserVO = new RiddingUserVO();
			riddingMap = new ConcurrentHashMap<String, RiddingUser>();
			riddingUserVO.setMap(riddingMap);
		}
		riddingMap = riddingUserVO.getMap();
		riddingMap.put(userKey, riddingUser);
		riddingUserVO.setSaveCacheTime(new Date().getTime());
		riddingUsersMap.put(riddingKey, riddingUserVO);
	}

	/**
	 * 删除骑行用户信息
	 * 
	 * @param riddingKey
	 * @param userKey
	 */
	public static void remove(long riddingKey, String userKey) {
		RiddingUserVO riddingUserVO = riddingUsersMap.get(riddingKey);
		if (riddingUserVO != null) {
			ConcurrentHashMap<String, RiddingUser> riddingMap = riddingUserVO.getMap();
			riddingMap.remove(userKey);
		}
		riddingUserVO.setSaveCacheTime(new Date().getTime());
	}

	/**
	 * 同步map
	 * 
	 * @param riddingKey
	 * @return
	 */
	public static ConcurrentHashMap<String, RiddingUser> getRiddingMap(long riddingKey) {
		RiddingUserVO riddingUserVO = riddingUsersMap.get(riddingKey);
		if (riddingUserVO == null) {
			return null;
		}
		return riddingUserVO.getMap();
	}
}

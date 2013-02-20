package com.ridding.security;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;

import com.ridding.util.DesUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-25 下午06:16:41 Class Description
 */
public class MyInterfaceTokenUtil {
	private static final Logger logger = Logger.getLogger(MyInterfaceTokenUtil.class);
	public static ConcurrentHashMap<Long, MyToken> map = new ConcurrentHashMap<Long, MyToken>();

	public static final String TOKENAUTH = "authToken";

	private static final String CODECHAR_STRING = "#####";

	/**
	 * 生成新的userToken
	 * 
	 * @param userId
	 * @param createTime
	 * @return
	 */
	public static MyToken genearteNewToken(Long userId, long createTime) {
		MyToken myToken = new MyToken();
		myToken.setLastUpdateTime(createTime);
		String encodeString = String.valueOf(userId) + CODECHAR_STRING + createTime;
		DesUtil desUtil = new DesUtil(TOKENAUTH);
		String token = desUtil.getEncString(encodeString);
		myToken.setToken(token);
		map.put(userId, myToken);
		return myToken;
	}

	/**
	 * 得到token
	 * 
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request) {
		String token = request.getHeader(TOKENAUTH);
		if (token == null) {
			token = ServletRequestUtils.getStringParameter(request, TOKENAUTH, null);
		}
		return token == null ? null : token;
	}

	/**
	 * 检测含token的用户
	 * 
	 * @param request
	 * @return
	 */
	public static Long getUserId(HttpServletRequest request) {
		String token = MyInterfaceTokenUtil.getToken(request);
		if (token == null) {
			return null;
		}
		token = token.trim();
		String[] tokens = MyInterfaceTokenUtil.getTokens(token);

		try {
			return Long.valueOf(tokens[0]);
		} catch (Exception e) {
			logger.error(tokens.toString() + "   " + tokens[0]);
			return null;
		}
	}

	/**
	 * 得到token中的信息
	 * 
	 * @param token
	 * @return
	 */
	public static String[] getTokens(String token) {
		DesUtil desUtil = new DesUtil(TOKENAUTH);
		String decode = desUtil.getDecString(token);
		return StringUtils.split(decode, CODECHAR_STRING);
	}

	/**
	 * 检测token中是否是当前请求用户
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkUser(HttpServletRequest request, Long requestUserId) {
		Long userId = MyInterfaceTokenUtil.getUserId(request);
		if (userId == null) {
			return false;
		}
		return requestUserId == userId;
	}

	/**
	 * 更新用户token失效时间
	 * 
	 * @param userId
	 */
	public static void updateUser(HttpServletRequest request, Long requestUserId) {
		Long userId = MyInterfaceTokenUtil.getUserId(request);
		String token = MyInterfaceTokenUtil.getToken(request);
		MyToken myToken = map.get(userId);
		long now = new Date().getTime();
		if (myToken == null) {
			myToken = new MyToken();
			myToken.setLastUpdateTime(now);
			myToken.setToken(token);
			map.put(userId, myToken);
		} else {
			myToken.setLastUpdateTime(now);
		}
	}

	/**
	 * 检测失效时间
	 * 
	 * @return
	 */
	public static boolean checkTime(Long userId, HttpServletRequest request) {
//		long now = new Date().getTime();
//		MyToken myToken = map.get(userId);
//		long tokenTime = MyInterfaceTokenUtil.getTimeFromToken(request);
//		if (myToken == null && tokenTime > 0 && TimeUtil.getBetween(now, tokenTime) > 48) {
//			return false;
//		}
//		if (myToken != null && TimeUtil.getBetween(now, myToken.getLastUpdateTime()) > 48) {
//			return false;
//		}
		return true;
	}

	/**
	 * 得到token中的时间
	 * 
	 * @param request
	 * @return
	 */
	public static long getTimeFromToken(HttpServletRequest request) {
		String token = MyInterfaceTokenUtil.getToken(request);
		if (token == null) {
			return -1;
		}
		String[] tokens = MyInterfaceTokenUtil.getTokens(token);
		if (ArrayUtils.isEmpty(tokens) && tokens.length < 2) {
			return -1;
		}
		try {
			return Long.valueOf(tokens[1]);
		} catch (Exception e) {
			logger.info("");
			return -1;
		}

	}
}

package com.ridding.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.ServletRequestUtils;

import weibo4j.Account;
import weibo4j.Friendships;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

import com.ridding.constant.SourceType;
import com.ridding.constant.SystemConst;
import com.ridding.mapper.SourceAccountMapper;
import com.ridding.meta.Profile;
import com.ridding.meta.SourceAccount;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.CookieUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-9 下午03:51:12 Class Description
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public static final String VALIDATE_CODE = "validateCode";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String ACCESSUSERID = "accessUserId";
	public static final String ACCESSTOKEN = "accessToken";
	public static final String SOURCETYPE = "sourceType";
	public static final String USERTOKEN = "userToken";

	@Resource
	private SourceAccountMapper sourceAccountMapper;

	@Resource
	private TransactionService transactionService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		this.setContinueChainBeforeSuccessfulAuthentication(true);
		String userName = obtainUsername(request);
		String passWord = obtainPassword(request);

		if (this.isFromInner(request)) {
			// Account account = accountMapper.getAccountByUserName(userName);
			// if (account == null || account.getPassWord() != passWord) {
			// throw new
			// AuthenticationServiceException("Authentication userName passWord not right input="
			// + passWord + " and  output ="
			// + account.getPassWord());
			// } else {
			// // 用户名验证成功
			// }
		} else if (this.isFromSource(request)) {
			try {
				this.getSourceAccountFromRequest(request);
			} catch (Exception e) {
				try {
					e.printStackTrace();
					response.sendRedirect(SystemConst.getValue("HOST") + "/");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}
			String accessToken = request.getAttribute("accesstoken") == null ? "" : request.getAttribute("accesstoken").toString();
			Integer sourceType = Integer.valueOf(request.getAttribute("sourcetype").toString());
			Long accessUserId = Long.valueOf(request.getAttribute("sourceid").toString());
			if (accessToken == null || sourceType == null || accessUserId == null) {
				throw new AuthenticationServiceException("Authentication accessToken accessUserId not right accessUserId=" + accessUserId
						+ " and  accessToken =" + accessToken + "sourceType" + sourceType);
			} else {
				this.checkSourceUser(accessToken, accessUserId, sourceType);
				userName = String.valueOf(accessUserId) + "#" + accessToken + "#" + sourceType;
				passWord = accessToken;
			}
		}
		// UsernamePasswordAuthenticationToken实现 Authentication
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, passWord);
		// 允许子类设置详细属性

		setDetails(request, authRequest);
		// 插入认证请求信息

		SecurityContextHolder.getContext().setAuthentication(authRequest);
		// 做重定向
		this.doRedirect(request, response);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 根据请求地址做重定向
	 * 
	 * @param request
	 * @param response
	 */
	private void doRedirect(HttpServletRequest request, HttpServletResponse response) {
		Integer sourceType = Integer.valueOf(request.getAttribute("sourcetype").toString());
		Long accessUserId = Long.valueOf(request.getAttribute("sourceid").toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", sourceType);
		map.put("accessUserId", accessUserId);
		SourceAccount sourceAccount = sourceAccountMapper.getSourceAccountByAccessUserId(map);
		if (sourceAccount == null) {
			try {
				response.sendRedirect(SystemConst.getValue("HOST") + "/");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		if (request.getQueryString().contains("mobileSinaLoginCallBack")) {
			MyToken myToken = MyInterfaceTokenUtil.genearteNewToken(sourceAccount.getUserId(), new Date().getTime());
			String token = myToken.getToken();
			long userId = sourceAccount.getUserId();
			request.setAttribute("authToken", token);
			((SavedRequestAwareAuthenticationSuccessHandler) successHandler).setDefaultTargetUrl(SystemConst.getValue("HOST")
					+ "/bind/mobilesinabind/show/?userId=" + userId + "&authToken=" + token);
		} else {
			((SavedRequestAwareAuthenticationSuccessHandler) successHandler).setDefaultTargetUrl(SystemConst.getValue("HOST") + "/user/"
					+ sourceAccount.getUserId() + "/ridding/list/");
		}
		this.friendshipUser(sourceAccount.getAccessToken(), sourceAccount.getAccessUserId());
		this.setAuthenticationSuccessHandler(successHandler);
		return;
	}

	/**
	 * 通过请求获得SourceAccount,目前只做新浪
	 * 
	 * @param request
	 * @return
	 */
	private void getSourceAccountFromRequest(HttpServletRequest request) throws Exception {
		String code = ServletRequestUtils.getStringParameter(request, "code", null);
		Oauth oauth = new Oauth();
		AccessToken accessToken = oauth.getAccessTokenByCode(code);
		if (accessToken == null) {

			throw new Exception("getSourceAccountFromRequest error where accessToken =null ");
		}
		Weibo weibo = new Weibo();
		weibo.setToken(accessToken.getAccessToken());
		Account am = new Account();
		JSONObject uid = am.getUid();
		Long sourceId = Long.valueOf(uid.getLong("uid"));
		request.setAttribute("accesstoken", accessToken.getAccessToken());
		request.setAttribute("sourceid", sourceId);
		request.setAttribute("sourcetype", SourceType.SINAWEIBO.getValue());
	}

	/**
	 * 关注骑去哪儿网
	 * 
	 * @param accessToken
	 */
	private void friendshipUser(String accessToken, long accessUserId) {
		if (accessUserId != 2672248903L) {
			Friendships friendships = new Friendships();
			try {
				friendships.createFriendshipsById("2672248903");
			} catch (WeiboException e) {
				// logger.error("MyUsernamePasswordAuthenticationFilter friendshipUser error");
			}
		}
	}

	/**
	 * 是否来自内部账号系统
	 * 
	 * @param request
	 * @return
	 */
	private boolean isFromInner(HttpServletRequest request) {
		String userName = obtainUsername(request);
		String passWord = obtainPassword(request);
		return !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(passWord);
	}

	/**
	 * 是否来自于外域
	 * 
	 * @return
	 */
	private boolean isFromSource(HttpServletRequest request) {
		String action = ServletRequestUtils.getStringParameter(request, "action", null);
		// 一个来自于web登陆，一个来自于手机新浪登陆
		return action.equals("sinaLoginCallBack") || action.equals("mobileSinaLoginCallBack");
	}

	/**
	 * 
	 * @param request
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionValidateCode = obtainSessionValidateCode(session);
		// 让上一次的验证码失效
		session.setAttribute(VALIDATE_CODE, null);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new AuthenticationServiceException("validateCode.notEquals");
		}
	}

	/**
	 * 检查资源是否正确
	 */
	private boolean checkSourceUser(String accessToken, long accessUserId, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", sourceType);
		map.put("accessUserId", accessUserId);
		SourceAccount sourceAccount = sourceAccountMapper.getSourceAccountByAccessUserId(map);
		if (sourceAccount != null) {
			// 如果之前登陆过
			if (!sourceAccount.getAccessToken().equals(accessToken)) {
				sourceAccount.setAccessToken(accessToken);
				return sourceAccountMapper.updateSourceAccount(sourceAccount) > 0;
			}
		} else {
			return this.insertNewUser(accessToken, sourceType, accessUserId);
		}
		return false;
	}

	/**
	 * 添加新用户
	 * 
	 * @param accessToken
	 * @param sourceType
	 * @param accessUserId
	 * @return
	 */
	private boolean insertNewUser(String accessToken, int sourceType, long accessUserId) {
		// 从来没有登陆过，添加用户
		SourceAccount sourceAccount = new SourceAccount();
		long nowTime = new Date().getTime();
		sourceAccount.setCreateTime(nowTime);
		sourceAccount.setAccessUserId(accessUserId);
		sourceAccount.setSourceType(sourceType);
		sourceAccount.setAccessToken(accessToken);
		try {
			Users users = new Users();
			User user = users.showUserById(String.valueOf(accessUserId));

			Profile profile = new Profile();
			profile.setNickName(user.getScreenName());
			profile.setbAvatorUrl(user.getavatarLarge());
			profile.setsAvatorUrl(user.getProfileImageUrl());
			return transactionService.insertSourceAccount(sourceAccount, profile) != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		Object obj = request.getParameter(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	protected String obtainSessionValidateCode(HttpSession session) {
		Object obj = session.getAttribute(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(USERNAME);
		return null == obj ? "" : obj.toString().trim();
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(PASSWORD);
		return null == obj ? "" : obj.toString();
	}

	public static String obtainUserToken(HttpServletRequest request) {
		Object obj = request.getHeader(USERTOKEN);
		if (obj == null) {
			Cookie cookie = CookieUtil.getCookie(request, MyUsernamePasswordAuthenticationFilter.USERTOKEN);
			if (cookie != null) {
				return cookie.getValue();
			}
			return null;
		}
		return obj.toString();
	}
}

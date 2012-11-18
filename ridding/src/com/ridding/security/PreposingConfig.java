package com.ridding.security;

import java.util.List;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-7 下午11:18:03 Class Description
 */
public class PreposingConfig {

	private List<String> noAuthURIConfig;

	private List<String> noAdminURIConfig;

	private PathMatcher urlMatcher = new AntPathMatcher();

	public static final String ADMINRULE = "ROLE_ADMIN";

	public static final String USERRULE = "ROLE_USER";

	/**
	 * 得到需要认证的用户列表
	 * 
	 * @param userType
	 * @return
	 */
	public List<String> getURIConfigByUser(String userType) {
		if (userType.equals("NONE")) {
			return noAuthURIConfig;
		} else if (userType.equals("ROLE_USER")) {
			return noAdminURIConfig;
		} else {
			return null;
		}
	}

	/**
	 * 判断是否需要验证
	 * 
	 * @param uri
	 * @return
	 */
	public boolean isNoAuthURI(String uri) {
		for (String ptn : this.noAuthURIConfig) {
			if (this.urlMatcher.match(ptn, uri))
				return true;
		}
		return false;
	}

	/**
	 * 判断是否需要验证
	 * 
	 * @param uri
	 * @return
	 */
	public boolean isNoAdminURI(String uri) {
		for (String ptn : this.noAdminURIConfig) {
			if (this.urlMatcher.match(ptn, uri))
				return true;
		}
		return false;
	}

	/**
	 * 返回对应需要资源的名称
	 * 
	 * @param url
	 * @return
	 */
	public String returnResourceAuth(String url) {
		if (this.isNoAuthURI(url)) {
			return null; // 不需要认证
		}
		if (this.isNoAdminURI(url)) {
			return PreposingConfig.USERRULE;
		}
		return PreposingConfig.ADMINRULE;
	}

	public List<String> getNoAuthURIConfig() {
		return noAuthURIConfig;
	}

	public void setNoAuthURIConfig(List<String> noAuthURIConfig) {
		this.noAuthURIConfig = noAuthURIConfig;
	}

	public List<String> getNoAdminURIConfig() {
		return noAdminURIConfig;
	}

	public void setNoAdminURIConfig(List<String> noAdminURIConfig) {
		this.noAdminURIConfig = noAdminURIConfig;
	}

}

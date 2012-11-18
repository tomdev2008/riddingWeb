package com.ridding.security;

import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-8 下午03:42:52 Class Description
 */
public class MyUser extends User implements CredentialsContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户userId
	 */
	private long userId;

	/**
	 * 登陆的token
	 */
	private String accessToken;
	/**
	 * 外域资源用户id
	 */
	private long accessUserId;
	/**
	 * 外域资源类型
	 */
	private String sourceType;

	public MyUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, long userId) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId = userId;
	}

	public long getAccessUserId() {
		return accessUserId;
	}

	public void setAccessUserId(long accessUserId) {
		this.accessUserId = accessUserId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}

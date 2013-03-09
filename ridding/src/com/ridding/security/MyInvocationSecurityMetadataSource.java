package com.ridding.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-7 下午01:27:35 Class Description
 */
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	/**
	 * 地址判断器
	 */
	private PreposingConfig preposingConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.springframework.security.access.SecurityMetadataSource#
	 * getAllConfigAttributes()
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.SecurityMetadataSource#getAttributes
	 * (java.lang.Object)
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// guess object is a URL. 
		String url = ((FilterInvocation) object).getRequestUrl();
		String returnUserRole = preposingConfig.returnResourceAuth(url);
		if (returnUserRole == null) {
			return null;
		}
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		atts.add(new SecurityConfig(returnUserRole));
		return atts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.SecurityMetadataSource#supports(java
	 * .lang.Class)
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	public PreposingConfig getPreposingConfig() {
		return preposingConfig;
	}

	public void setPreposingConfig(PreposingConfig preposingConfig) {
		this.preposingConfig = preposingConfig;
	}

}

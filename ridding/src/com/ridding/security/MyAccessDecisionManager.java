package com.ridding.security;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-7 下午01:27:28 Class Description 决定url是否满足用户登陆权限
 */
public class MyAccessDecisionManager implements AccessDecisionManager {
	private static final Logger logger = Logger.getLogger(MyAccessDecisionManager.class);

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException {
		if (configAttributes == null) {
			return;
		}

		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		FilterInvocation filterInvocation = (FilterInvocation) object;
		while (ite.hasNext()) {
			ConfigAttribute ca = ite.next();
			String needRole = ((SecurityConfig) ca).getAttribute();
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (needRole.equals(ga.getAuthority())) { // ga is user's role.
					return;
				}
			}
			if ((needRole.equals(PreposingConfig.USERRULE) || needRole.equals(PreposingConfig.ADMINRULE)) && filterInvocation != null) {
				long requestUserId = ServletRequestUtils.getLongParameter(filterInvocation.getRequest(), "userId", -1L);
				if (MyInterfaceTokenUtil.checkUser(filterInvocation.getRequest(), requestUserId)) {
					if (MyInterfaceTokenUtil.checkTime(requestUserId, filterInvocation.getRequest())) {
						MyInterfaceTokenUtil.updateUser(filterInvocation.getRequest(), requestUserId);
						return;
					}
				}
			}
		}
		throw new AccessDeniedException("no right");
	}

	@Override
	public boolean supports(ConfigAttribute arg0) {
		return true;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}

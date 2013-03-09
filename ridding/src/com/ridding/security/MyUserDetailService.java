package com.ridding.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.SourceAccountMapper;
import com.ridding.meta.SourceAccount;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-7 下午01:27:19 Class Description
 */
public class MyUserDetailService implements UserDetailsService {

	@Resource
	private SourceAccountMapper sourceAccountMapper;

	@Resource
	private ProfileMapper profileMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		String[] splits = StringUtils.split(username, "#");
		// 说明是外域资源
		if (splits != null && splits.length == 3) {
			try {
				long accessUserId = Long.valueOf(splits[0]);
				String accessToken = splits[1];
				int sourceType = Integer.valueOf(splits[2]);
				Map<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("sourceType", sourceType);
				hashMap.put("accessUserId", accessUserId);
				SourceAccount sourceAccount = sourceAccountMapper.getSourceAccountByAccessUserId(hashMap);
				Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
				GrantedAuthorityImpl auth2 = new GrantedAuthorityImpl("ROLE_USER");
				auths.add(auth2);
				MyUser myUser = new MyUser(username, accessToken, true, true, true, true, auths, sourceAccount.getUserId());
				((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).setDetails(myUser);
				return myUser;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
			GrantedAuthorityImpl auth2 = new GrantedAuthorityImpl("ROLE_USER");
			auths.add(auth2);
			MyUser myUser = new MyUser(username, "123", true, true, true, true, auths, 123L);
			((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).setDetails(myUser);
			return myUser;
		}
	}
}

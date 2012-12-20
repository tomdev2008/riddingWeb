package com.ridding.web.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.ridding.meta.Profile;
import com.ridding.security.MyUser;
import com.ridding.service.ProfileService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@corp.netease.com
 * @version CreateTime2012-3-5 01:45:43 Class Description
 */
public abstract class AbstractBaseController extends MultiActionController {
	@Resource(name = "paramResolver")
	private MethodNameResolver methodNameResolver;

	@Resource
	private ProfileService profileService;

	/**
	 * 初始化
	 */
	@PostConstruct
	public void baseInit() {
		super.setMethodNameResolver(methodNameResolver);
	}

	/**
	 * 插入用户信息
	 * 
	 * @param mv
	 * @param userd
	 */
	protected void setUD(ModelAndView mv, long hostUserId, long visitUserId) {
		Profile hostProfile = profileService.getProfile(hostUserId);
		if (hostProfile == null) {
			return;
		}
		mv.addObject("hostUserId", hostUserId);
		mv.addObject("hostSAvatorUrl", hostProfile.getsAvatorUrl());
		mv.addObject("hostNickname", hostProfile.getNickName());
		double distance = hostProfile.getTotalDistance() * 1.0 / 2.0;
		distance = ((int) (distance * 100)) / 100 * 1000;
		mv.addObject("totalDistance", distance);
		if (visitUserId > 0) {
			Profile visitProfile = profileService.getProfile(visitUserId);
			if (visitProfile == null) {
				return;
			}
			mv.addObject("visitUserId", visitUserId);
			mv.addObject("visitSAvatorUrl", visitProfile.getsAvatorUrl());
			mv.addObject("visitBAvatorUrl", visitProfile.getbAvatorUrl());
			mv.addObject("visitNickname", visitProfile.getNickName());
			mv.addObject("visitLevel", visitProfile.getLevel());
			MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
			mv.addObject("userAccessToken", myUser.getAccessToken());
			if (hostProfile.getUserId() == visitProfile.getUserId()) {
				mv.addObject("isMe", true);
			}
		}

	}

	/**
	 * 不经过安全框架
	 * 
	 * @param mv
	 * @param hostUserId
	 */
	protected void setNoHostUD(ModelAndView mv, long hostUserId) {
		Profile hostProfile = profileService.getProfile(hostUserId);
		if (hostProfile == null) {
			return;
		}
		mv.addObject("hostUserId", hostUserId);
		mv.addObject("hostSAvatorUrl", hostProfile.getsAvatorUrl());
		mv.addObject("hostNickname", hostProfile.getNickName());
	}
}

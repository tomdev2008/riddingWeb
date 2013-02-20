package com.ridding.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.security.MyUser;
import com.ridding.service.MapService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-3 下午11:32:50 Class Description web端的controller
 */
@Controller("riddingWebController")
public class RiddingWebController extends AbstractBaseController {
	@Resource
	private RiddingService riddingService;

	@Resource
	private MapService mapService;

	@Resource
	private ProfileService profileService;

	

	/**
	 * 创建骑行活动，包括地图
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView createRidding(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = ServletRequestUtils.getLongParameter(request, "userId");
		ModelAndView mv = new ModelAndView("mapCreate");
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
		this.setUD(mv, userId, myUser.getUserId());
		return mv;
	}


}

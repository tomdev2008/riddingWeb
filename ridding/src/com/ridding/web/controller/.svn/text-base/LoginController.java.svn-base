package com.ridding.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-5 下午02:38:39 Class Description 登陆controller
 */
@Controller("loginController")
public class LoginController extends AbstractBaseController {
	private static final Logger logger = Logger.getLogger(LoginController.class);

	@Resource
	private ProfileService profileService;
	@Resource
	private RiddingService riddingService;

	/**
	 * backend web登陆页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("loginView");
	}

	/**
	 * 通过接口登录,外部资源,更新资源，账户信息必然存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView mobileSinaLoginCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	/**
	 * 通过接口登录,内部资源
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView loginInterfaceByInner(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/**
	 * web端新浪微博回调登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView sinaLoginCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
}

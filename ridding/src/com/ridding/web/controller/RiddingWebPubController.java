package com.ridding.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.constant.SystemConst;
import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.security.MyUser;
import com.ridding.service.MapService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-3 下午11:32:59 Class Description web端公开controller
 */
@Controller("riddingWebPubController")
public class RiddingWebPubController extends AbstractBaseController {

	@Resource
	private RiddingService riddingService;

	@Resource
	private MapService mapService;

	@Resource
	private ProfileService profileService;

	private static final Logger logger = Logger.getLogger(RiddingWebPubController.class);

	public ModelAndView webIndex(HttpServletRequest request, HttpServletResponse response) {
		String agent = request.getHeader("User-Agent");
		if (agent.toLowerCase().contains("ipad") ||agent.toLowerCase().contains("iphone") || agent.toLowerCase().contains("android")) {
			return new ModelAndView("webIndex_Iphone");

		} else {
			return new ModelAndView("webIndex");
		}
	}

	/**
	 * 跳转到新浪授权登陆页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView sinaLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			response.sendRedirect("https://api.weibo.com/oauth2/authorize?client_id=" + SystemConst.getValue("WEBAPPKEY") + "&redirect_uri="
					+ SystemConst.getValue("HOST") + "/bind/sinabind/callback/&response_type=code");
			return null;
		} catch (IOException e) {
			response.sendRedirect(SystemConst.getValue("HOST") + "/");
			return null;
		}
	}

	/**
	 * 下载app
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void downLoadApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InputStream fisInputStream = new BufferedInputStream(new FileInputStream(getServletContext().getRealPath("/app/骑去哪儿.ipa")));
		byte[] buffer1 = new byte[1024 * 4];
		response.reset();
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		response.resetBuffer();
		response.setCharacterEncoding("utf-8");
		response.setHeader("Connection", "close");
		response.setHeader("Content-Disposition", "attachment;filename=ridding.ipa");
		int n = 0;
		while (-1 != (n = fisInputStream.read(buffer1))) {
			toClient.write(buffer1, 0, n);
		}
		fisInputStream.close();
		toClient.close();
	}

	/**
	 * 某用户的骑行页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView riddingListView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = ServletRequestUtils.getLongParameter(request, "userId");
		Profile profile = profileService.getProfile(userId);
		if (profile == null) {
			response.sendRedirect(SystemConst.getValue("HOST") + "/");
			return null;
		}
		ModelAndView mv = new ModelAndView("riddingList");
		int count = riddingService.getRiddingCount(userId);
		mv.addObject("riddingCount", count);
//		List<ActivityRidding> riddingUserlList = riddingService.getRiddingListbyUserId(userId, 0, 0);
//		mv.addObject("riddingUserList", riddingUserlList);
//		long visitorUserId = -1L;
//		try {
//			MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
//			visitorUserId = myUser.getUserId();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		this.setUD(mv, userId, visitorUserId);
		return mv;
	}

	/**
	 * 显示骑行活动页，如果是队长，可以编辑，如果是队员。不能编辑
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showRidding(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = ServletRequestUtils.getLongParameter(request, "userId", 0L);
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", 0L);
		RiddingUser riddingUser = riddingService.getRiddingUser(riddingId, userId);
		if (riddingId <= 0 || riddingUser == null) {
			response.sendRedirect(SystemConst.getValue("HOST") + "/user/" + userId + "/ridding/list/");
			return null;
		}
		Ridding ridding = riddingService.getRidding(riddingId);
		if (ridding == null) {
			response.sendRedirect(SystemConst.getValue("HOST") + "/user/" + userId + "/ridding/list/");
			return null;
		}
		IMap iMap = mapService.getMapById(ridding.getMapId(), IMap.Using);
		iMap.getMidLocationList();
		iMap.getCenterLocation();
		ModelAndView mv = new ModelAndView();
		mv.addObject("iMap", iMap);
		mv.addObject("ridding", ridding);
		long visitorUserId = -1L;
		try {
			MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
			visitorUserId = myUser.getUserId();
		} catch (Exception e) {
		}
		this.setUD(mv, userId, visitorUserId);
		if (visitorUserId > 0) {
			RiddingUser riddingUser2 = riddingService.getRiddingUser(riddingId, visitorUserId);
			if (riddingUser2 != null && riddingUser2.isLeader()) {
				SourceAccount sourceAccount = profileService.getSourceAccountByUserIdsSourceType(visitorUserId, 1);
				if (sourceAccount != null) {
					mv.addObject("userAccessToken", sourceAccount.getAccessToken());
				}
				mv.setViewName("mapCreate");
			} else {
				mv.setViewName("mapCreate_1");
			}
		} else {
			mv.setViewName("mapCreate_1");
		}
		return mv;
	}

	public ModelAndView appdownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mvAndView = new ModelAndView("appDownload");
		return mvAndView;
	}
}

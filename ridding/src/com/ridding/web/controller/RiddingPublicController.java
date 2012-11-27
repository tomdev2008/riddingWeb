package com.ridding.web.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.constant.SystemConst;
import com.ridding.constant.returnCodeConstance;
import com.ridding.mapper.MapFixMapper;
import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.vo.ProfileVO;
import com.ridding.service.MapService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;
import com.ridding.util.http.HttpJsonUtil;
import com.ridding.util.http.HttpServletUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-4-11 04:16:24 Class Description
 */
@Controller("riddingPublicController")
public class RiddingPublicController extends AbstractBaseController {

	@Resource
	private ProfileService profileService;

	@Resource
	private MapFixMapper mapFixMapper;

	@Resource
	private RiddingService riddingService;

	@Resource
	private MapService mapService;

	/**
	 * 得到用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView getUserProfile(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		Long userId = ServletRequestUtils.getLongParameter(request, "userId");
		Integer sourceType = ServletRequestUtils.getIntParameter(request, "sourceType");
		JSONObject returnObject = new JSONObject();
		Profile profile = profileService.getProfile(userId);
		SourceAccount sourceAccount = profileService.getSourceAccountByUserIdsSourceType(userId, sourceType);
		if (profile == null || sourceAccount == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			return mv;
		}
		returnObject.put("userid", profile.getUserId());
		returnObject.put("username", profile.getUserName());
		returnObject.put("nickname", profile.getNickName());
		returnObject.put("bavatorurl", profile.getbAvatorUrl());
		returnObject.put("savatorurl", profile.getsAvatorUrl());
		returnObject.put("totalDistance", profile.getTotalDistance());
		returnObject.put("code", returnCodeConstance.SUCCESS);
		if (sourceAccount != null) {
			returnObject.put("sourceid", sourceAccount.getAccessUserId());
			returnObject.put("accessToken", sourceAccount.getAccessToken());
		}
		int count = riddingService.getRiddingCount(userId);
		returnObject.put("riddingCount", count);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 得到骑行列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getRiddingList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		logger.info(jsonString);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		Ridding ridding = null;
		try {
			ridding = HttpServletUtil.parseToRidding(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			return mv;
		}
		List<RiddingUser> riddingUserList = riddingService.getSelfRiddingUserList(userId, ridding.getLimit(), ridding.getCreateTime(), ridding
				.isLarger());
		HttpJsonUtil.setRiddingList(returnObject, riddingUserList);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 *得到骑行地图或者编译前地址
	 * 
	 * @return
	 */
	public ModelAndView getRidingMapOrLocation(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		Ridding ridding = riddingService.getRidding(ridingId);
		if (ridding == null) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGUSER);
			return mv;
		}
		IMap iMap = mapService.getMapByRiddingId(ridingId);
		HttpJsonUtil.setRiddingMapOrTaps(returnObject, iMap);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 返回修复的经纬度
	 * 
	 * @return
	 */
	public ModelAndView getFixedCoordinate(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		float latitude = ServletRequestUtils.getFloatParameter(request, "latitude", -1);
		float longtitude = ServletRequestUtils.getFloatParameter(request, "longtitude", -1);
		MapFix mapFix = mapService.getMapFix(latitude, longtitude);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		returnObject.put("realLatitude", mapFix.getLatitude());
		returnObject.put("realLongtitude", mapFix.getLongtitude());
		mv.addObject("returnObject", returnObject.toString());
		return mv;
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
			response.sendRedirect("https://api.weibo.com/oauth2/authorize?client_id=" + SystemConst.getValue("WEBAPPKEY")
					+ "&display=mobile&response_type=code&redirect_uri=" + SystemConst.getValue("HOST") + "/bind/mobilesinabind/callback/");
			return null;
		} catch (IOException e) {
			response.sendRedirect(SystemConst.getValue("HOST") + "/");
			return null;
		}
	}

	/**
	 * 得到骑行用户列表,个人信息，新浪微博信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getRiddingUserList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		List<ProfileVO> profileVOs = riddingService.getRiddingUserListToProfile(ridingId, -1, 0);
		HttpJsonUtil.setProfileList(returnObject, profileVOs);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 重定向返回
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView mobileSinaLoginShow(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("return");
	}

	/**
	 * 得到已经上传的图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getuploadedPhotos(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		List<RiddingPicture> riddingPictures = riddingService.getRiddingPictureByUserIdRiddingId(riddingId, userId);
		HttpJsonUtil.setupLoadedRiddingPicture(returnObject, riddingPictures);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}
	

}

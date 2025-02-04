package com.ridding.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.bean.dwr.DwrRiddingShareBean;
import com.ridding.constant.RiddingQuitConstant;
import com.ridding.constant.SourceType;
import com.ridding.constant.SystemConst;
import com.ridding.constant.returnCodeConstance;
import com.ridding.meta.ApnsDevice;
import com.ridding.meta.IMap;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingAction;
import com.ridding.meta.RiddingComment;
import com.ridding.meta.RiddingGps;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.RiddingUser;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.UserPay;
import com.ridding.meta.RiddingAction.RiddingActionResponse;
import com.ridding.meta.RiddingAction.RiddingActions;
import com.ridding.meta.UserPay.UserPayType;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.meta.vo.ProfileSourceFeed;
import com.ridding.meta.vo.UserRelationVO;
import com.ridding.security.MyUser;
import com.ridding.service.FeedbackService;
import com.ridding.service.IOSApnsService;
import com.ridding.service.MapService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingCommentService;
import com.ridding.service.RiddingService;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.UserNearbyService;
import com.ridding.service.UserPayService;
import com.ridding.service.UserRelationService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.http.HttpServletUtil;
import com.ridding.util.http.HttpServletUtil2;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-5 11:27:43 Class Description
 */
@Controller("riddingController")
public class RiddingController extends AbstractBaseController {

	private static final Logger logger = Logger.getLogger(RiddingController.class);

	@Resource
	private RiddingService riddingService;

	@Resource
	private MapService mapService;

	@Resource
	private ProfileService profileService;

	@Resource
	private IOSApnsService iosApnsService;
	@Resource
	private TransactionService transactionService;
	@Resource
	private DwrRiddingShareBean dwrRiddingShareBean;
	@Resource
	private RiddingCommentService riddingCommentService;

	@Resource
	private UserRelationService userRelationService;

	@Resource
	private UserNearbyService userNearbyService;

	@Resource
	private FeedbackService feedbackService;

	@Resource
	private UserPayService userPayService;

	@Resource
	private SinaWeiBoService sinaWeiBoService;

	/**
	 * 得到骑行用户信息，返回骑行数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView showRiddingView(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		logger.info(jsonString);
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingUser user = riddingService.getRiddingUser(ridingId, userId);
		if (user == null || !user.isTeamer()) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGUSER);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		RiddingUser riddingUser = null;
		try {
			riddingUser = HttpServletUtil.parseToRidding4RiddingView(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			logger.error("RiddingController showRiddingView parseToRidding4RiddingView error where json=" + jsonString);
		}
		long time = new Date().getTime();
		riddingUser.setUserId(userId);
		riddingUser.setRiddingId(ridingId);
		riddingUser.setCacheTime(time);

		List<RiddingUser> ridingUserList = riddingService.getAllRiddingUserList(riddingUser);
		userNearbyService.asyncUpdateUserNearBy(userId, riddingUser.getLatitude(), riddingUser.getLongtitude());
		JSONArray dataArray = HttpServletUtil2.parseShowRiddingView(ridingUserList);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 插入骑行编译前地址
	 * 
	 * @deprecated 老版本功能
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView setRidingMapLocation(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8").trim();
		logger.info(jsonString);
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingUser riddingUser = riddingService.getRiddingUser(ridingId, userId);
		if (riddingUser == null || !riddingUser.isTeamer()) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGUSER);
			return mv;
		}
		IMap iMap = null;
		try {
			iMap = HttpServletUtil.parseTosetRidingMapLocation(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		IMap oldMap = mapService.getMapById(iMap.getId(), IMap.Using);
		if (StringUtils.isEmpty(oldMap.getMapPoint())) {
			oldMap.setMapPoint(iMap.getMapPoint());
			mapService.updateRiddingMap(oldMap);
		}
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 删除骑行或者结束骑行
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView RiddingAction(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long riddingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int type = ServletRequestUtils.getIntParameter(request, "type", -1);
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		Profile profile = profileService.getProfile(userId);
		if (profile == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		RiddingActionResponse actionResponse = RiddingActionResponse.Fail;
		switch (RiddingActions.genRiddingAction(type)) {
		case Like:
			actionResponse = riddingService.incRiddingLike(riddingId, userId);
			break;
		case Care:
			actionResponse = riddingService.incRiddingCare(riddingId, userId);
			break;
		case Finished:
			boolean succ = riddingService.endRiddingByLeader(riddingId, userId);
			if (succ) {
				actionResponse = RiddingActionResponse.SUCC;
			}
			break;
		case Use:
			actionResponse = riddingService.incRiddingUse(riddingId, userId);
			if (actionResponse == RiddingActionResponse.SUCC) {
				succ = dwrRiddingShareBean.useOthersRidding(riddingId, userId);
				if (!succ) {
					actionResponse = RiddingActionResponse.Fail;
				}
			}
			break;
		default:
			break;
		}
		switch (actionResponse) {
		case DoubleDo:
			returnObject.put("code", returnCodeConstance.RiddingActionDouble);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		case InRidding:
			returnObject.put("code", returnCodeConstance.RiddingActionInMyRidding);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		case Fail:
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		default:
			break;
		}
		Ridding ridding = riddingService.getRidding(riddingId);
		RiddingAction riddingAction = riddingService.getUserAction(userId, riddingId);
		JSONObject dataObject = HttpServletUtil2.parseRiddingAction(ridding, riddingAction);
		returnObject.put("data", dataObject);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 喜欢骑行中的某张图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView RiddingLikePic(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long objectId = ServletRequestUtils.getLongParameter(request, "objectId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingActionResponse actionResponse = riddingService.incPicLike(riddingId, userId, objectId);
		switch (actionResponse) {
		case DoubleDo:
			returnObject.put("code", returnCodeConstance.RiddingActionDouble);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		case InRidding:
			returnObject.put("code", returnCodeConstance.RiddingActionInMyRidding);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		case Fail:
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		default:
			break;
		}
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 添加骑行用户,同时删除用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView addRiddingUsers(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		logger.info(jsonString);
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int sourceType = ServletRequestUtils.getIntParameter(request, "sourceType", -1);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingUser riddingUser = riddingService.getRiddingUser(ridingId, userId);
		if (riddingUser == null || !riddingUser.isLeader()) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGLEADER);
			return mv;
		}
		List<Profile> profileList;
		try {
			profileList = HttpServletUtil.parseToAddAccount(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		riddingService.insertRiddingUsers(profileList, ridingId, sourceType, userId);

		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 删除骑行用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView deleteRiddingUsers(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		logger.info(jsonString);
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingUser riddingUser = riddingService.getRiddingUser(ridingId, userId);
		if (riddingUser == null || !riddingUser.isLeader()) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGLEADER);
			return mv;
		}
		List<Long> deleteList;
		try {
			deleteList = HttpServletUtil.parseToDeleteAccount(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		riddingService.deleteRiddingUsers(deleteList, ridingId);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 退出某个骑行活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView quitRidding(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingUser riddingUser = riddingService.getRiddingUser(ridingId, userId);
		if (riddingUser == null) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGUSER);
			return mv;
		}
		RiddingQuitConstant constant = riddingService.quitRidding(userId, ridingId);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		if (constant == RiddingQuitConstant.Leader) {
			returnObject.put("code", returnCodeConstance.RIDDINGLEADER);
		} else if (constant == RiddingQuitConstant.Failed) {
			returnObject.put("code", returnCodeConstance.FAILED);
		}
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 通过userid和对应开放平台得到对应用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getUserPublicMessage(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		logger.info(jsonString);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		ProfileSourceFeed profileSourceFeed;
		try {
			profileSourceFeed = HttpServletUtil.parseToProfileSourceFeed(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		List<SourceAccount> sourceAccounts = profileService.getSourceAccountByUserIdsSourceType(profileSourceFeed.getUserIdList(), profileSourceFeed
				.getSourceType());
		JSONArray dataArray = HttpServletUtil2.parseGetUserPublicMessage(sourceAccounts);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 退出登陆接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView quit(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException {
		response.setContentType("text/html;charset=UTF-8");
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		if (myUser == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		Long userId = ServletRequestUtils.getLongParameter(request, "userId");
		Integer sourceType = ServletRequestUtils.getIntParameter(request, "sourceType");
		SourceAccount sourceAccount = profileService.getSourceAccountByUserIdsSourceType(userId, sourceType);
		if (sourceAccount == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 第一次登陆推荐
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView firstRecom(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		if (myUser == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		String cityName = ServletRequestUtils.getStringParameter(request, "cityname", null);
		List<IMap> recomMaps = mapService.getRecomMaps(cityName);
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 添加iphone token
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView addApnsToken(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		String token = ServletRequestUtils.getStringParameter(request, "token", null);
		String version = ServletRequestUtils.getStringParameter(request, "version", null);
		int isPro = ServletRequestUtils.getIntParameter(request, "ispro", 0);
		ApnsDevice apnsDevice = new ApnsDevice();
		apnsDevice.setUserId(userId);
		apnsDevice.setToken(token);
		apnsDevice.setVersion(version);
		apnsDevice.setIsPro(isPro);
		long time = new Date().getTime();
		apnsDevice.setCreateTime(time);
		apnsDevice.setLastUpdateTime(time);
		apnsDevice.setStatus(ApnsDevice.VALID);
		if (iosApnsService.addIosApns(apnsDevice)) {
			returnObject.put("code", returnCodeConstance.SUCCESS);
		} else {
			returnObject.put("code", returnCodeConstance.FAILED);
		}
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 上传骑行图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView uploadRiddingPhotos(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		RiddingPicture riddingPicture;
		try {
			riddingPicture = HttpServletUtil.parseToRiddingPicture(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		riddingPicture.setUserId(userId);
		riddingPicture.setRiddingId(riddingId);
		riddingPicture.setBreadId(0);
		if (!riddingService.addRiddingPicture(riddingPicture)) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 新建骑行活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView addRiddingMap(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		IMap iMap = null;
		Ridding ridding = new Ridding();
		try {
			logger.info("addRiddingMap json=" + jsonString + " userId=" + userId);
			iMap = HttpServletUtil.parseFromMap(jsonString, ridding);
			iMap.setUserId(userId);
			ridding.setLeaderUserId(userId);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		iMap.setStatus(IMap.Using);
		iMap.setObjectType(SourceType.WebApi.getValue());
		iMap.setObjectId(0);
		iMap.setCreateTime(new Date().getTime());
		if (!transactionService.insertANewRidding(iMap, ridding)) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		returnObject.put("riddingId", ridding.getId());
		returnObject.put("imageUrl", SystemConst.getValue("IMAGEHOST") + iMap.getUrlKey());

		JSONObject dataObject = HttpServletUtil2.parseAddRiddingMap(ridding, SystemConst.getValue("IMAGEHOST") + iMap.getUrlKey());
		returnObject.put("data", dataObject);
		// 由于以前的bug，这里code 用200, 一直到v1.2以下版本才做更新
		returnObject.put("code", 200);
		returnObject.put("code1", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 添加骑行评论
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView addRiddingComment(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		logger.info(jsonString);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		RiddingComment riddingComment = null;
		try {
			riddingComment = HttpServletUtil.parseToRiddingComment(jsonString);
			riddingComment.setUserId(userId);
			riddingComment.setRiddingId(riddingId);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		boolean succ = riddingCommentService.addRiddingComment(riddingComment);
		if (!succ) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		JSONObject dataObject = HttpServletUtil2.parseAddRiddingComment(riddingComment);
		returnObject.put("data", dataObject);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 得到某人骑行操作记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getRiddingActions(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		long riddingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		logger.info(jsonString);
		ModelAndView mv = new ModelAndView("return");
		Ridding ridding = riddingService.getRidding(riddingId);
		RiddingAction riddingAction = riddingService.getUserAction(userId, riddingId);
		JSONObject dataObject = HttpServletUtil2.parseRiddingAction(ridding, riddingAction);
		returnObject.put("data", dataObject);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 更新用户的背景
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView updateUserBackground(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		ModelAndView mv = new ModelAndView("return");
		Profile profile = null;
		try {
			profile = HttpServletUtil.parseUpdateBackground(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		boolean succ = profileService.updateBackgroundUrl(profile.getBackgroundUrl(), userId);
		if (!succ) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		profile = profileService.getProfile(userId);
		JSONObject dataObject = HttpServletUtil2.parseGetUserProfile(profile, null, -1);
		returnObject.put("data", dataObject);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 好友申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView requestToAddRelation(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long toUserId = ServletRequestUtils.getLongParameter(request, "toUserId", -1L);
		if (userRelationService.addUserRelation(userId, toUserId) == RiddingActionResponse.SUCC) {
			returnObject.put("code", returnCodeConstance.SUCCESS);
		}
		returnObject.put("code", returnCodeConstance.FAILED);
		mv.addObject("returnObject", returnObject.toString());
		logger.info(returnObject);
		return mv;
	}

	/**
	 * 添加或者删除用户关系
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView removeOrAddUserRelation(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long toUserId = ServletRequestUtils.getLongParameter(request, "toUserId", -1L);
		int status = ServletRequestUtils.getIntParameter(request, "status", 0);
		RiddingActionResponse actionResponse = userRelationService.removeOrAddUserRelation(userId, toUserId, status);
		if (actionResponse == RiddingActionResponse.SUCC) {
			returnObject.put("code", returnCodeConstance.SUCCESS);
			mv.addObject("returnObject", returnObject.toString());
			logger.info(returnObject);
			return mv;
		}
		returnObject.put("code", returnCodeConstance.FAILED);
		mv.addObject("returnObject", returnObject.toString());
		logger.info(returnObject);
		return mv;
	}

	/**
	 * 获取好友列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getUserRelationList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 0);
		int offset = ServletRequestUtils.getIntParameter(request, "offset", 0);
		List<UserRelationVO> userRelationVOs = userRelationService.getUserRelations(userId, limit, offset);
		JSONArray dataArray = HttpServletUtil2.parseUserRelationVOs(userRelationVOs);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 得到附近的用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView showNearbyUsers(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		if (userId < 0) {
			logger.error("userId=" + userId + " is wrong!");
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		List<Profile> userNearbyProfiles = userNearbyService.showUserNearbyList(userId);
		if (userNearbyProfiles == null) {
			logger.error("There is no userNearby!");
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		JSONArray jsonArray = HttpServletUtil2.parseShowNearbyUsers(userNearbyProfiles);
		returnObject.put("data", jsonArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 发送我当前的位置，保存到数据库
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView sendMyLocation(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		double latitude = ServletRequestUtils.getDoubleParameter(request, "latitude", 0.0);
		double longitude = ServletRequestUtils.getDoubleParameter(request, "longitude", 0.0);
		if (userId < 0) {
			logger.error("The userId=" + userId + " is wrong!");
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		} else if ((latitude > 90) || (latitude < -90) || (longitude > 180) || (longitude) < -180) {
			logger.error("The latitude=" + latitude + ",the longitude=" + longitude + "is wrong!");
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}

		boolean hasAddOrUpdate = userNearbyService.addOrUpdateUsersNearby(userId, latitude, longitude);
		if (!hasAddOrUpdate) {
			logger.error("addOrUpdateUsersNearby is failed!");
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 删除骑行照片
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView removeRiddingPicture(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		long pictureId = ServletRequestUtils.getLongParameter(request, "pictureId", -1L);
		if (pictureId < 0) {
			logger.error("The pictureId=" + pictureId + " is wrong!");
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}

		boolean succ = riddingService.removeRiddingPicture(pictureId);
		if (!succ) {
			logger.error("deletePicture is failed! where pictureId=" + pictureId);
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 显示附近的用户，骑行活动 添加反馈
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView showNearBy(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		JSONObject returnObject = new JSONObject();
		double latitude = ServletRequestUtils.getDoubleParameter(request, "latitude", -1.0);
		double longitude = ServletRequestUtils.getDoubleParameter(request, "longitude", -1.0);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int limit = ServletRequestUtils.getIntParameter(request, "limit", -1);
		int offset = ServletRequestUtils.getIntParameter(request, "offset", -1);
		int type = ServletRequestUtils.getIntParameter(request, "type", 0);
		userNearbyService.asyncUpdateUserNearBy(userId, latitude, longitude);
		if (type == 1) {
			// 附近的骑行活动
			List<ActivityRidding> riddingUserList = riddingService.getNearByRiddingList(userId, latitude, longitude, limit, offset);
			JSONArray jsonArray = HttpServletUtil2.parseGetRiddingList(riddingUserList);
			returnObject.put("data", jsonArray.toString());
		} else {

		}

		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 添加反馈
	 * 
	 * @auther zyslovely@gmail.com
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView addFeedback(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long userQQ = ServletRequestUtils.getLongParameter(request, "qq", -1L);
		String userMail = ServletRequestUtils.getStringParameter(request, "mail", "");
		String description = ServletRequestUtils.getStringParameter(request, "description", "");
		String deviceVersion = ServletRequestUtils.getStringParameter(request, "deviceversion", "");
		String version = ServletRequestUtils.getStringParameter(request, "iosversion", "");
		String appVersion = ServletRequestUtils.getStringParameter(request, "appversion", "");

		if (StringUtils.isEmpty(description)) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		if (feedbackService.addFeedback(userId, userQQ, userMail, description, deviceVersion, version, appVersion)) {
			returnObject.put("code", returnCodeConstance.SUCCESS);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		returnObject.put("code", returnCodeConstance.FAILED);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 更新wifi同步状态
	 * 
	 * @auther zyslovely@gmail.com
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView updateRiddingWifiSyncOrGps(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userid", -1L);
		int yes = ServletRequestUtils.getIntParameter(request, "yes", -1);
		int type = ServletRequestUtils.getIntParameter(request, "type", 0);
		Ridding ridding = riddingService.getRidding(riddingId);
		if (ridding == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		if (type == 0) {
			riddingService.updateRiddingSyncWifi(riddingId, yes, userId);
		} else if (type == 1) {
			riddingService.updateRiddingGps(userId, riddingId, yes);
		}
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 显示用户支付信息
	 * 
	 * @auther zyslovely@gmail.com
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView showUserPay(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int type = ServletRequestUtils.getIntParameter(request, "typeId", -1);
		List<UserPay> userPayList;
		if (type > 0) {
			userPayList = new ArrayList<UserPay>();
			UserPay userPay = userPayService.getUserPayByUserId(userId, type);
			userPayList.add(userPay);
		} else {
			userPayList = userPayService.getUserPaysValid(userId);
		}
		JSONArray jsonArray = HttpServletUtil2.parseShowUserPays(userPayList);
		returnObject.put("data", jsonArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	/**
	 * 试用操作
	 * 
	 * @auther zyslovely@gmail.com
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView tryUserPay(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int type = ServletRequestUtils.getIntParameter(request, "typeId", -1);
		UserPay userPay = userPayService.getUserPayByUserId(userId, type);
		if (userPay != null) {
			returnObject.put("code", returnCodeConstance.UserPayCanNotTry);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		userPay = new UserPay();
		userPay.setUserId(userId);
		userPay.setTypeId(type);
		userPay.setDayLong(15);// 试用15天
		userPay.setStatus(UserPay.status_trying);
		userPay = userPayService.addUserPay(userPay);
		JSONObject jsonObject = HttpServletUtil2.parseShowUserPay(userPay);
		if (type == UserPayType.Weather.getValue()) {
			SourceAccount sourceAccount = profileService.getSourceAccountByUserIdsSourceType(userId, SourceType.SINAWEIBO.getValue());
			sinaWeiBoService.sendSinaWeiBo("我正在使用 骑行者的天气服务，非常好用，推荐给大家。快速了解骑行路线天气状况，动态调整出行。@骑行者rider 下载地址【 http://t.cn/zj79l1z 】", sourceAccount
					.getAccessToken(), "local/weibo/weather_20130327.jpg");
		}
		returnObject.put("data", jsonObject);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}

	public ModelAndView addRiddingGps(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1);
		String mapPoints = ServletRequestUtils.getStringParameter(request, "mapPoints", null);
		int distance = ServletRequestUtils.getIntParameter(request, "distance", -1);
		RiddingUser riddingUser = riddingService.getRiddingUser(riddingId, userId);
		if (riddingUser == null || StringUtils.isEmpty(mapPoints)) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}

		RiddingGps riddingGps = new RiddingGps();
		riddingGps.setRiddingId(riddingId);
		riddingGps.setUserId(userId);
		riddingGps.setMapPoint(mapPoints);
		riddingGps.setDistance(distance);
		riddingGps.setCreateTime(new Date().getTime());

		riddingGps = riddingService.addRiddingGps(riddingGps);
		if (riddingGps == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
		} else {
			returnObject.put("code", returnCodeConstance.SUCCESS);
		}
		mv.addObject("returnObject", returnObject.toString());
		return mv;
	}
}

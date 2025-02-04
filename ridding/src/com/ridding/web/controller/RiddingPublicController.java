package com.ridding.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import weibo4j.Account;
import weibo4j.Weibo;

import com.ridding.constant.SystemConst;
import com.ridding.constant.returnCodeConstance;
import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingAction;
import com.ridding.meta.RiddingComment;
import com.ridding.meta.RiddingGps;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.Ridding.RiddingType;
import com.ridding.meta.RiddingAction.RiddingActions;
import com.ridding.meta.vo.ActivityRidding;
import com.ridding.meta.vo.ProfileVO;
import com.ridding.meta.vo.UserRelationVO;
import com.ridding.service.MapService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingCommentService;
import com.ridding.service.RiddingService;
import com.ridding.service.UserRelationService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;
import com.ridding.util.http.HttpServletUtil;
import com.ridding.util.http.HttpServletUtil2;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-4-11 04:16:24 Class Description
 */
@Controller("riddingPublicController")
public class RiddingPublicController extends AbstractBaseController {

	@Resource
	private ProfileService profileService;

	@Resource
	private RiddingService riddingService;

	@Resource
	private MapService mapService;

	@Resource
	private RiddingCommentService riddingCommentService;

	@Resource
	private UserRelationService userRelationService;

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
		int checkRegister = ServletRequestUtils.getIntParameter(request, "register", -1);
		JSONObject returnObject = new JSONObject();
		Profile profile = profileService.getProfile(userId);
		SourceAccount sourceAccount = profileService.getSourceAccountByUserIdsSourceType(userId, sourceType);
		if (profile == null || sourceAccount == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			return mv;
		}
		if (checkRegister < 0) {
			try {
				Weibo weibo = new Weibo();
				weibo.setToken(sourceAccount.getAccessToken());
				Account am = new Account();
				weibo4j.org.json.JSONObject uid = am.getUid();
			} catch (Exception e) {
				// 获取其他用户信息是，可能这个用户的token已经失效了
				// returnObject.put("code", returnCodeConstance.TOKENEXPIRED);
				// mv.addObject("returnObject", returnObject.toString());
				// return mv;
			}
		}
		int count = riddingService.getRiddingCount(userId);
		JSONObject dataObject = HttpServletUtil2.parseGetUserProfile(profile, sourceAccount, count);
		returnObject.put("data", dataObject);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.info(returnObject);
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

		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		Ridding ridding = null;
		try {
			ridding = HttpServletUtil.parseToRidding(jsonString);
		} catch (Exception e) {
			logger.error(jsonString);
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			return mv;
		}
		List<ActivityRidding> riddingUserList = riddingService.getSelfRiddingUserList(userId, ridding.getLimit(), ridding.getCreateTime(), ridding
				.isLarger());
		JSONArray jsonArray = HttpServletUtil2.parseGetRiddingList(riddingUserList);
		returnObject.put("data", jsonArray.toString());
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 得到骑行地图或者编译前地址
	 * 
	 * @return
	 */
	public ModelAndView getRidingMapOrLocation(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		long ridingId = ServletRequestUtils.getLongParameter(request, "ridingId", -1L);
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		Ridding ridding = riddingService.getRidding(ridingId);
		if (ridding == null) {
			returnObject.put("code", returnCodeConstance.NOTRIDINGUSER);
			return mv;
		}
		IMap iMap;
		if (ridding.getRiddingType() == RiddingType.Farway.getValue()) {
			iMap = mapService.getMapByRiddingId(ridingId);
		} else {
			RiddingGps riddingGps = riddingService.getRiddingGps(userId, ridingId);
			iMap = new IMap();
			iMap.setMapPoint(riddingGps.getMapPoint());
		}

		JSONObject dataObject = HttpServletUtil2.parseGetRidingMapOrLocation(iMap);
		returnObject.put("data", dataObject.toString());
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 返回修复的经纬度
	 * 
	 * @return
	 */
	public ModelAndView getFixedCoordinate(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		ModelAndView mv = new ModelAndView("return");
		double latitude = ServletRequestUtils.getDoubleParameter(request, "latitude", -999999);
		double longtitude = ServletRequestUtils.getDoubleParameter(request, "longtitude", -999999);
		JSONObject returnObject = new JSONObject();
		if (latitude != -999999 && longtitude != -999999) {
			MapFix mapFix = mapService.getMapFix(latitude, longtitude);
			returnObject.put("code", returnCodeConstance.SUCCESS);
			JSONObject dataObject = HttpServletUtil2.parseGetFixedCoordinate(mapFix, latitude, longtitude);
			returnObject.put("data", dataObject.toString());
			mv.addObject("returnObject", returnObject.toString());
		} else {
			String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
			try {

				List<MapFix> list = HttpServletUtil.parseMapFixs(jsonString);
				List<MapFix> returnList = new ArrayList<MapFix>(list.size());
				for (MapFix mapFix : list) {
					MapFix returnMapFix = mapService.getMapFix(mapFix.getLatitude(), mapFix.getLongtitude());
					returnList.add(returnMapFix);
				}
				JSONArray dataArray = HttpServletUtil2.parseMapFixList(returnList);
				returnObject.put("data", dataArray);
				returnObject.put("code", returnCodeConstance.SUCCESS);
				mv.addObject("returnObject", returnObject.toString());
				logger.info(returnObject.toString());
			} catch (Exception e) {
				logger.error(jsonString);
				returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
				e.printStackTrace();
				return mv;
			}
		}

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
		returnObject.put("code", returnCodeConstance.SUCCESS);
		JSONArray dataArray = HttpServletUtil2.parseGetRiddingUserList(profileVOs);
		returnObject.put("data", dataArray);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
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
		// V1.3及以下版本 没有userId。
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		int limit = ServletRequestUtils.getIntParameter(request, "limit", -1);
		long lastUpdateTime = ServletRequestUtils.getLongParameter(request, "lastupdatetime", -1);
		if (lastUpdateTime < 0) {
			lastUpdateTime = 0;
		}
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");
		List<RiddingPicture> riddingPictures = riddingService.getRiddingPictureByRiddingId(riddingId, limit, lastUpdateTime);
		if (!ListUtils.isEmptyList(riddingPictures)) {
			List<Long> userids = new ArrayList<Long>(riddingPictures.size());
			for (RiddingPicture riddingPicture : riddingPictures) {
				userids.add(riddingPicture.getUserId());
			}
			List<Profile> profileList = profileService.getProfileList(userids);
			Map<Long, Profile> profileMap = HashMapMaker.listToMap(profileList, "getUserId", Profile.class);
			List<RiddingAction> actions = riddingService.getRiddingActionsByTypeUserId(riddingId, RiddingActions.LikePicture.getValue(), userId);
			Map<Long, RiddingAction> riddingActionMap = HashMapMaker.listToMap(actions, "getObjectId", RiddingAction.class);
			for (RiddingPicture riddingPicture : riddingPictures) {
				Profile profile = profileMap.get(riddingPicture.getUserId());
				if (profile != null) {
					riddingPicture.setProfile(profile);
				}
				RiddingAction action = riddingActionMap.get(riddingPicture.getId());
				if (action != null) {
					riddingPicture.setLiked(true);
				} else {
					riddingPicture.setLiked(false);
				}
			}
		}
		JSONArray dataArray = HttpServletUtil2.parseGetuploadedPhotos(riddingPictures);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 得到进行中的骑行活动，根据时间排序，或者推荐的
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getGoingRiddings(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();

		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8").trim();

		ModelAndView mv = new ModelAndView("return");
		Ridding ridding = null;
		try {
			ridding = HttpServletUtil.parseToRiddingByLastUpdateTime(jsonString);
		} catch (Exception e) {
			logger.error("riddingPublicController getGoingRiddings where input str=" + jsonString);
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			return mv;
		}
		if (ridding == null) {
			logger.error("riddingPublicController getGoingRiddings is null where jsonString=" + jsonString);
			returnObject.put("code", returnCodeConstance.FAILED);
			return mv;
		}
		List<Ridding> riddingList = null;
		if (ridding.isRecom == 1) {
			riddingList = riddingService.getRecomRiddingList(ridding.getaPublic().getWeight(), ridding.getLimit(), ridding.isLarger());
		} else {
			riddingList = riddingService.getRiddingListByLastUpdateTime(ridding.getLastUpdateTime(), ridding.getLimit(), ridding.isLarger(),
					ridding.isRecom);
		}
		JSONArray dataArray = HttpServletUtil2.parseGetGoingRiddings(riddingList);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 得到骑行评论
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getRiddingComments(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		String jsonString = HttpServletUtil.parseRequestAsString(request, "utf-8");
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		logger.info(jsonString);
		ModelAndView mv = new ModelAndView("return");
		RiddingComment riddingComment = null;
		try {
			riddingComment = HttpServletUtil.parseToCommentByLastCreateTime(jsonString);
		} catch (Exception e) {
			returnObject.put("code", returnCodeConstance.INNEREXCEPTION);
			e.printStackTrace();
			return mv;
		}
		List<RiddingComment> riddingComments = riddingCommentService.getRiddingComments(riddingId, riddingComment.getLastCreateTime(), riddingComment
				.getLimit(), riddingComment.isLarger());
		JSONArray dataArray = HttpServletUtil2.parseRiddingComment(riddingComments);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

	/**
	 * 得到某个用户的关注关系
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getUserRelations(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		long userId = ServletRequestUtils.getLongParameter(request, "userId", -1L);
		ModelAndView mv = new ModelAndView("return");
		int limit = 100, offset = 0;
		List<UserRelationVO> list = userRelationService.getUserRelations(userId, limit, offset);

		JSONArray dataArray = HttpServletUtil2.parseUserRelationVOs(list);
		returnObject.put("data", dataArray);
		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.debug(returnObject);
		return mv;
	}

}

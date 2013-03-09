package com.ridding.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.constant.returnCodeConstance;
import com.ridding.service.ClubService;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-19 上午12:47:25 Class Description
 */
@Controller("riddingClubController")
public class RiddingClubController extends AbstractBaseController {
	private static final Logger logger = Logger
			.getLogger(RiddingClubController.class);

	@Resource
	private ClubService clubService;

	/**
	 * 创建俱乐部申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView applyForCreateClub(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");

		long userId = ServletRequestUtils.getLongParameter(request, "userId",
				-1L);
		String clubName = ServletRequestUtils.getStringParameter(request,
				"clubName", "");
		String clubLocation = ServletRequestUtils.getStringParameter(request,
				"clubLocation", "");
		String clubDescription = ServletRequestUtils.getStringParameter(
				request, "clubDescription", "");

		if (userId < 0 || clubName == null || clubLocation == null
				|| clubDescription == null) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			logger.error("There is something wrong with userId = " + userId
					+ ",clubName = " + clubName + ",clubLocation = "
					+ clubLocation + ",clubDescription = " + clubDescription);
			return mv;
		}

		if (!clubService.applyForCreateClub(userId, clubName, clubLocation,
				clubDescription)) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			logger.error("Fail to apply for creating club with userId = "
					+ userId + ",clubName = " + clubName + ",clubLocation = "
					+ clubLocation + ",clubDescription = " + clubDescription);
			return mv;
		}

		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.info(returnObject);
		return mv;
	}

	/**
	 * 加入俱乐部申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView applyForJoinClub(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject returnObject = new JSONObject();
		ModelAndView mv = new ModelAndView("return");

		long userId = ServletRequestUtils.getLongParameter(request, "userId",
				-1L);
		long clubId = ServletRequestUtils.getLongParameter(request, "clubId",
				-1L);

		if (userId < 0 || clubId < 0) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			logger.error("There is something wrong with userId = " + userId
					+ ",clubId = " + clubId);
			return mv;
		}

		if (!clubService.applyForJoinClub(userId, clubId)) {
			returnObject.put("code", returnCodeConstance.FAILED);
			mv.addObject("returnObject", returnObject.toString());
			logger.error("Fail to apply for joining club with userId = "
					+ userId + ",clubId = " + clubId);
			return mv;
		}

		returnObject.put("code", returnCodeConstance.SUCCESS);
		mv.addObject("returnObject", returnObject.toString());
		logger.info(returnObject);
		return mv;
	}
}

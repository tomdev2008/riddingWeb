package com.ridding.web.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.constant.SystemConst;
import com.ridding.meta.Profile;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.WeiBo;
import com.ridding.security.MyUser;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.SourceService;
import com.ridding.util.ListUtils;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-9 上午10:33:36 Class Description
 */
@Controller("backendController")
public class BackendController extends AbstractBaseController {

	@Resource
	private SourceService sourceService;

	@Resource
	private SinaWeiBoService sinaWeiBoService;

	@Resource
	private ProfileService profileService;

	@Resource
	private RiddingService riddingService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView indexBackend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("backendIndex");
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
		Profile profile = profileService.getProfile(myUser.getUserId());
		if (profile.getLevel() != 1) {
			response.sendRedirect(SystemConst.getValue("HOST"));
			return null;
		}
		this.setUD(mv, myUser.getUserId(), myUser.getUserId());
		return mv;
	}

	/**
	 * 检查url
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sendWeiBo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("sendWeiBo");
		List<WeiBo> sendList = sinaWeiBoService.getWeiBoList();
		long visitUserId = ServletRequestUtils.getLongParameter(request, "userId");
		mv.addObject("weiboList", sendList);
		this.setUD(mv, visitUserId, visitUserId);
		return mv;
	}

	/**
	 * 推荐
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView huodongRecom(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("huodongRecom");
		int weight = ServletRequestUtils.getIntParameter(request, "weight", -1);
		if (weight <= 0) {
			weight = 99999;
		}
		List<Ridding> riddingList = riddingService.getRecomRiddingList(weight, 50, false);
		if (!ListUtils.isEmptyList(riddingList)) {
			for (Ridding ridding : riddingList) {
				List<RiddingPicture> pictureList = riddingService.getRiddingPictureByRiddingId(ridding.getId(), 50, new Date().getTime());
				ridding.setRiddingPictureList(pictureList);
			}
		}
		long visitUserId = ServletRequestUtils.getLongParameter(request, "userId");
		mv.addObject("riddingList", riddingList);
		this.setUD(mv, visitUserId, visitUserId);
		return mv;
	}

	/**
	 * 后台得到活动列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView huodongList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("huodongList");
		int limit = 10;
		long requestTime = ServletRequestUtils.getLongParameter(request, "requestTime", -1L);
		if (requestTime < 0) {
			requestTime = new Date().getTime();
		}
		int nextOrBefore = ServletRequestUtils.getIntParameter(request, "nextOrBefore", 0);
		boolean isLarge = nextOrBefore > 0;
		long userId = ServletRequestUtils.getLongParameter(request, "userid", -1L);
		List<Ridding> riddingList = riddingService.getRiddingListByLastUpdateTime(requestTime, limit, isLarge, 0);
		if (ListUtils.isEmptyList(riddingList)) {
			mv.addObject("topUpdateTime", -1);
			mv.addObject("bottomUpdateTime", -1);
			return mv;
		}
		int pictureLimit = 3;
		for (Ridding ridding : riddingList) {
			long riddingId = ridding.getId();
			List<RiddingPicture> riddingPictures = riddingService.getRiddingPictureByRiddingId(riddingId, pictureLimit, requestTime);
			if (!ListUtils.isEmptyList(riddingPictures)) {
				ridding.setRiddingPictureList(riddingPictures);
			}
		}
		mv.addObject("riddingList", riddingList);

		mv.addObject("topUpdateTime", riddingList.get(0).getLastUpdateTime());
		mv.addObject("bottomUpdateTime", riddingList.get(riddingList.size() - 1).getLastUpdateTime());
		return mv;
	}

	/**
	 * 得到骑行活动信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView backendHuodong(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("backendHuodong");

		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		if (riddingId < 0) {
			logger.error("riddingId<0!");
		}
		Ridding ridding = riddingService.getRidding(riddingId);
		if (ridding == null) {
			return mv;
		}
		Date data = new Date();
		long requestTime = data.getTime();
		List<RiddingPicture> riddingPictures = riddingService.getRiddingPictureByRiddingId(riddingId, 0, requestTime);
		mv.addObject("riddingPictures", riddingPictures);
		mv.addObject("ridding", ridding);
		return mv;
	}

	/**
	 * 获得某个骑行活动的评论
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView backendHuodongComments(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("return");
		long riddingId = ServletRequestUtils.getLongParameter(request, "riddingId", -1L);
		if (riddingId < 0) {
			logger.error("riddingId<0!");
		}
		return mv;
	}
}

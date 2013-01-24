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
		int offset = ServletRequestUtils.getIntParameter(request, "offset", -1);
		if (offset < 0) {
			offset = 0;
		}
		
		
		return mv;
	}
}

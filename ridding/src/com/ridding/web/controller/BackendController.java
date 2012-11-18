package com.ridding.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.meta.Source;
import com.ridding.meta.WeiBo;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.SourceService;

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

	/**
	 * 检查url
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView checkUrlView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("checkSinaWeiBo");
		int page = ServletRequestUtils.getIntParameter(request, "page", -1);
		if (page <= 0) {
			page = 1;
		}
		int limit = 100;
		int type = ServletRequestUtils.getIntParameter(request, "type", -1);
		int sourceType = ServletRequestUtils.getIntParameter(request, "sourceType", -1);
		if (sourceType < 0) {
			sourceType = 1;
		}
		if (type == 0) {
			// 得到被认定为url错误的新浪微博

			List<Source> sourceList = sourceService.getSourceListWithAccount(Source.UrlError, limit, (page - 1) * limit, sourceType);
			int sourceCount = sourceService.getSourceCountByStatus(Source.UrlError, sourceType);
			mv.addObject("sourceTotalCount", sourceCount);
			mv.addObject("sourceList", sourceList);
		} else if (type == 1) {
			// 得到已经被处理的，但是在map中不存在的新浪微博
			List<Source> sourceList = sourceService.getDealedNotInMap(Source.Dealed, limit, (page - 1) * limit, sourceType);
			mv.addObject("sourceList", sourceList);
			mv.addObject("sourceTotalCount", sourceList == null ? 0 : sourceList.size());
		}
		mv.addObject("page", page);
		mv.addObject("type", type);
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
}

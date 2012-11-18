package com.ridding.bean.dwr;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.meta.vo.ProfileVO;
import com.ridding.service.RiddingService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-7 下午02:18:23 Class Description
 */
@Service("dwrRiddingPubBean")
public class DwrRiddingPubBean {
	@Resource
	private RiddingService riddingService;

	/**
	 * 得到骑行的用户列表
	 * 
	 * @param riddingId
	 * @return
	 */
	public List<ProfileVO> riddingUserList(long riddingId) {
		List<ProfileVO> profileVOs = riddingService.getRiddingUserListToProfile(riddingId, 0, 0);
		return profileVOs;
	}
}

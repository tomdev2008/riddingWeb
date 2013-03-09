package com.ridding.bean.dwr;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.meta.Profile;
import com.ridding.meta.UserPay;
import com.ridding.meta.UserPay.UserPayType;
import com.ridding.meta.vo.ProfileTaobaoVO;
import com.ridding.service.ProfileService;
import com.ridding.service.UserPayService;
import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-3 上午02:16:13 Class Description
 */
@Service("dwrUserPayBackendBean")
public class DwrUserPayBackendBean {
	private static final Logger logger = Logger.getLogger(DwrUserPayBackendBean.class);

	@Resource
	private UserPayService userPayService;

	@Resource
	private ProfileService profileService;

	/**
	 * 添加用户付费
	 * 
	 * @param userName
	 * @param typeId
	 * @param dayLong
	 * @return
	 */
	public boolean addUserPay(String taobaoCode, int typeId, int dayLong) {
		Profile profile = profileService.getProfileByTaobaoCode(taobaoCode);
		if (profile == null) {
			return false;
		}
		UserPayType type = UserPayType.genUserPayType(typeId);
		if (type == null) {
			return false;
		}
		UserPay userPay = new UserPay();
		userPay.setUserId(profile.getUserId());
		userPay.setDayLong(dayLong);
		userPay.setStatus(UserPay.status_valid);
		userPay.setTypeId(typeId);
		return userPayService.addUserPay(userPay) != null;
	}

	/**
	 * 通过taobaoCode的到用户信息
	 * 
	 * @param taobaoCode
	 * @return
	 */
	public ProfileTaobaoVO getProfileByTaobaoCode(String taobaoCode) {
		Profile profile = profileService.getProfileByTaobaoCode(taobaoCode);
		if (profile == null) {
			logger.error("DwrUserPayBackendBean ProfileTaobaoVO error where code=" + taobaoCode);
			return null;
		}
		List<UserPay> userPays = userPayService.getUserPaysValid(profile.getUserId());
		for (UserPay userPay : userPays) {
			userPay.setBeginTimeStr(TimeUtil.getFormatTime(userPay.getBeginTime()));
			long endTime = TimeUtil.getDayBefore(userPay.getBeginTime(), 0 - userPay.getDayLong());
			userPay.setEndTimeStr(TimeUtil.getFormatTime(endTime));
		}
		ProfileTaobaoVO profileTaobaoVO = new ProfileTaobaoVO();
		profileTaobaoVO.setProfile(profile);
		profileTaobaoVO.setUserPays(userPays);
		return profileTaobaoVO;
	}
}

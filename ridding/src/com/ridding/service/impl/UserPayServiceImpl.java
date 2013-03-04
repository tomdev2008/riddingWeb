package com.ridding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.UserPayMapper;
import com.ridding.meta.UserPay;
import com.ridding.service.UserPayService;
import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-3 上午02:11:20 Class Description
 */
@Service("userPayService")
public class UserPayServiceImpl implements UserPayService {

	@Resource
	private UserPayMapper userPayMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.UserPayService#addUserPay(com.ridding.meta.UserPay)
	 */
	public UserPay addUserPay(UserPay userPay) {
		UserPay pay = userPayMapper.getUserPayByUserId(userPay.getUserId(), userPay.getTypeId());
		if (pay == null) {
			long dayBreakTime = TimeUtil.getDaybreakTime();
			userPay.setBeginTime(dayBreakTime);
			userPay.setCreateTime(dayBreakTime);
			if (userPayMapper.addUserPay(userPay) > 0) {
				return userPay;
			}
		}
		// 如果已经无效了，重新设置
		if (pay.getStatus() == UserPay.status_invalid) {
			long dayBreakTime = TimeUtil.getDaybreakTime();
			pay.setBeginTime(dayBreakTime);
			pay.setDayLong(userPay.getDayLong());
			pay.setStatus(UserPay.status_valid);
			if (userPayMapper.updateUserPayByUserId(pay) > 0) {
				return userPay;
			}
		} else {
			// 如果处于试用期或者使用中，做添加
			int dayLong = pay.getDayLong();
			dayLong += userPay.getDayLong();
			pay.setDayLong(dayLong);
			pay.setStatus(UserPay.status_valid);
			if (userPayMapper.updateUserPayByUserId(pay) > 0) {
				return pay;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.UserPayService#getUserPaysValid(long)
	 */
	@Override
	public List<UserPay> getUserPaysValid(long userId) {
		return userPayMapper.getUserPaysValid(userId);
	}
}

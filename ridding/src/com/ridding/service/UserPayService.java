package com.ridding.service;

import java.util.List;

import com.ridding.meta.UserPay;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-3 上午02:11:10 Class Description
 */
public interface UserPayService {

	/**
	 * 添加付费
	 * 
	 * @param userPay
	 * @return
	 */
	public UserPay addUserPay(UserPay userPay);

	/**
	 * 得到用户付费列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPay> getUserPaysValid(long userId);
	

	/**
	 * 获取userPay
	 * 
	 * @auther zyslovely@gmail.com
	 * @param userId
	 * @param type
	 * @return
	 */
	public UserPay getUserPayByUserId(long userId, int type);
}

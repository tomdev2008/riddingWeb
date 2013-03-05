package com.ridding.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.UserPay;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-3 上午02:09:52 Class Description
 */
public interface UserPayMapper {
	/**
	 * 添加用户付费
	 * 
	 * @param userPay
	 * @return
	 */
	public int addUserPay(UserPay userPay);

	/**
	 * 得到这个用户的某个付费项目
	 * 
	 * @param userId
	 * @param typeId
	 * @return
	 */
	public UserPay getUserPayByUserId(@Param("userId") long userId, @Param("typeId") int typeId);

	/**
	 * 更新用户支付
	 * 
	 * @param userPay
	 * @return
	 */
	public int updateUserPayByUserId(UserPay userPay);

	/**
	 * 得到用户支付列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPay> getUserPaysValid(@Param("userId") long userId);

}

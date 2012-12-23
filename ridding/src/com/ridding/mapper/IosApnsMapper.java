package com.ridding.mapper;

import java.util.List;

import com.ridding.meta.ApnsDevice;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-1 下午11:32:16 Class Description
 */
public interface IosApnsMapper {
	/**
	 * 添加apns
	 * 
	 * @param apnsDevice
	 * @return
	 */
	public int addApnsDevice(ApnsDevice apnsDevice);

	/**
	 * 得到apns
	 * 
	 * @param userId
	 * @return
	 */
	public ApnsDevice getApnsDevice(long userId);

	/**
	 * 更新apns
	 * 
	 * @param apnsDevice
	 * @return
	 */
	public int updateApns(ApnsDevice apnsDevice);

	/**
	 * 
	 * @return
	 */
	public List<ApnsDevice> getAllApnsDevice();
}

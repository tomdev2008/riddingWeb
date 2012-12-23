package com.ridding.service;

import com.ridding.meta.ApnsDevice;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-1 下午06:25:58 Class Description
 */
public interface IOSApnsService {

	public boolean addIosApns(ApnsDevice apnsDevice);

	public void sendApns(String text);

	/**
	 * 发送用户评论
	 * 
	 * @param userId
	 * @param message
	 */
	public void sendUserApns(long userId, String message);

}

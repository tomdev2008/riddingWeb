package com.ridding.mapper;

import java.util.List;

import com.ridding.meta.WeiBoToBeSend;

/**
 * 
 * @author Administrator
 * 
 */
public interface WeiBoToBeSendMapper {
	/**
	 * 添加应发送微博账户
	 * 
	 * @param weiBoToBeSend
	 * @return
	 */
	public int addWeiBoToBeSend(WeiBoToBeSend weiBoToBeSend);

	/**
	 * 获取应发送微博账户列表
	 * 
	 * @return
	 */
	public List<WeiBoToBeSend> getWeiBoToBeSendList(int sendStatus);

	/**
	 * 更新应发送微博账户状态
	 * 
	 * @param weiBoToBeSend
	 * @return
	 */
	public int updateWeiBoToBeSend(WeiBoToBeSend weiBoToBeSend);

	/**
	 * 通过Id获取应发送微博账户列表
	 * 
	 * @param accountId
	 * @return
	 */
	public List<WeiBoToBeSend> getWeiBoToBeSendListByAccountId(long accountId);
}
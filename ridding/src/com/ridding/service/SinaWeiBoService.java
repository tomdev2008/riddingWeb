package com.ridding.service;

import java.util.List;

import com.ridding.meta.WeiBo;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime时间2012-3-19 04:59:23 Class Description
 */
public interface SinaWeiBoService {
	/**
	 * 得到新浪微博
	 */
	public void getAtMeSinaWeiBoQuartz();

	/**
	 * 
	 * @param userId
	 * @param accessToken
	 * @param since_id
	 * @param filter_by_author
	 * @param filter_by_source
	 * @param filter_by_type
	 * @param count
	 * @param page
	 */
	public void getAtMeSinaWeiBo(long userId, String accessToken, long since_id, int filter_by_author, int filter_by_source, int filter_by_type,
			int count, int page);

	/**
	 * 添加地图成功后回复评论
	 * 
	 * @param sinaWeiBoId
	 * @return
	 */
	public boolean sendSinaWeiBoCallBack(long sinaWeiBo, String comment);

	/**
	 * 测试新浪用户是否正确
	 * 
	 * @param accessUserId
	 * @param accessToken
	 * @return
	 */
	public boolean checkSinaWeiBoUser(long accessUserId, String accessToken);

	/**
	 * 得到发送的微博列表
	 * 
	 * @return
	 */
	public List<WeiBo> getWeiBoList();

	/**
	 * 添加需要发送的微博
	 * 
	 * @param weiBo
	 * @return
	 */
	public boolean addWeiBo(WeiBo weiBo);

	/**
	 * 发送新浪微博定时任务
	 */
	public void sendWeiBoQuartz();

	/**
	 * 发送新浪微博，带图片
	 * 
	 * @auther zyslovely@gmail.com
	 * @param text
	 * @param accessToken
	 * @param imageUrl
	 * @return
	 */
	public String sendSinaWeiBo(String text, String accessToken, String imageUrl);

	/**
	 * 将转发地图微博生成地图
	 */
	public void genMapFromMapWeiBoQuartz();

	/**
	 * 定时获取应发送微博
	 */
	public void getRiddingSinaWeiBoQuartz();

	/**
	 * 定时发送微博
	 * 
	 * @return
	 */
	public void sendWeiBoCommentQuartz();
}

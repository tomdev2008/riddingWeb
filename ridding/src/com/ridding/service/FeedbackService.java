package com.ridding.service;

import java.util.List;

import com.ridding.meta.Feedback;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-3-1 下午10:29:40 Class Description
 */
public interface FeedbackService {
	/**
	 * 添加反馈
	 * 
	 * @param feedback
	 * @return
	 */
	public boolean addFeedback(long userId, long userQQ, String userMail,
			String description, String deviceVersion, String version,
			String appVersion);

	/**
	 * 获取反馈列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Feedback> getFeedbackList();

	/**
	 * 回复反馈
	 * 
	 * @return
	 */
	public boolean replyFeedback(long id, long userId, String reply);
}

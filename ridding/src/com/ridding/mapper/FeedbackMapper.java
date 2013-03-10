package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.Feedback;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-3-1 下午10:14:21 Class Description
 */
public interface FeedbackMapper {
	/**
	 * 添加反馈
	 * 
	 * @param feedback
	 * @return
	 */
	public int addFeedback(Feedback feedback);

	/**
	 * 获取反馈列表
	 * 
	 * @return
	 */
	public List<Feedback> getFeedbackList();

	/**
	 * 通过id得到反馈
	 * 
	 * @auther zyslovely@gmail.com
	 * @param id
	 * @return
	 */
	public Feedback getFeedbackById(long id);

	/**
	 * 更新反馈
	 * 
	 * @param hashMap
	 * @return
	 */
	public int updateFeedback(Map<String, Object> map);
}

package com.ridding.service;

import java.util.List;

import com.ridding.meta.RiddingComment;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-8 下午11:41:07 Class Description
 */
public interface RiddingCommentService {
	/**
	 * 添加评论
	 * 
	 * @param riddingComment
	 * @return
	 */
	public boolean addRiddingComment(RiddingComment riddingComment);

	/**
	 * 得到评论列表
	 * 
	 * @param createTime
	 * @param limit
	 * @param isLarger
	 * @return
	 */
	public List<RiddingComment> getRiddingComments(long riddingId,
			long createTime, int limit, boolean isLarger);
	

	/**
	 * 递归删除骑行评论及其子评论
	 * 
	 * @param replyId
	 * @param count
	 * @return
	 */
	public void deleteRiddingCommentByReplyIdAndCount(long replyId);
}
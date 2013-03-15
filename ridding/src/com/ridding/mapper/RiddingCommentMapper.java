package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.RiddingComment;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-8 下午11:32:56 Class Description
 */
public interface RiddingCommentMapper {
	/**
	 * 添加骑行活动
	 * 
	 * @param riddingComment
	 * @return
	 */
	public int addRiddingComment(RiddingComment riddingComment);

	/**
	 * 添加评论
	 * 
	 * @param map
	 * @return
	 */
	public List<RiddingComment> getRiddingCommentList(Map<String, Object> map);

	/**
	 * 获取评论列表
	 * 
	 * @auther zyslovely@gmail.com
	 * @param createTime
	 * @param limit
	 * @param isLarger
	 * @return
	 */
	public List<RiddingComment> getRiddingCommentListByTime(@Param(value = "createTime") long createTime, @Param(value = "limit") int limit,
			@Param(value = "isLarger") int isLarger);

	/**
	 * 删除评论
	 * 
	 * @param commentId
	 * @return
	 */
	public int deleteRiddingComment(long commentId);

	/**
	 * 通过ReplyId得到评论
	 * 
	 * @param commentId
	 * @return
	 */
	public List<RiddingComment> getRiddingCommentListByReplyId(long replyId);

	/**
	 * 通过riddingId删除评论
	 * 
	 */
	public int deleteRiddingCommentByRiddingId(long riddingId);
}

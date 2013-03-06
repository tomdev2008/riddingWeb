package com.ridding.meta;

/**
 * @author yunshang_734@163.com E-mail:yunshang_734@163.com
 * @version CreateTime：2013-3-1 下午10:07:34 Class Description
 */
public class Feedback {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1256453488651L;
	/**
	 * id
	 */
	private long id;
	/**
	 * 反馈人id
	 */
	private long userId;
	/**
	 * 反馈人QQ
	 */
	private long userQQ;
	/**
	 * 反馈人邮箱
	 */
	private String userMail;
	/**
	 * 反馈时间
	 */
	private long createTime;
	/**
	 * 反馈内容
	 */
	private String description;
	/**
	 * 处理状态
	 */
	private int status;
	/**
	 * 回复时间
	 */
	private long replyTime;
	/**
	 * 回复内容
	 */
	private String reply;

	public enum FeedbackStatus {
		/**
		 * 未处理
		 */
		Undone {
			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 已处理
		 */
		Done {
			@Override
			public int getValue() {
				return 1;
			}
		};

		public abstract int getValue();

		public static FeedbackStatus genFeedbackStatus(int t) {
			for (FeedbackStatus status : FeedbackStatus.values()) {
				if (status.getValue() == t)
					return status;
			}
			return null;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserQQ() {
		return userQQ;
	}

	public void setUserQQ(long userQQ) {
		this.userQQ = userQQ;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(long replyTime) {
		this.replyTime = replyTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
}
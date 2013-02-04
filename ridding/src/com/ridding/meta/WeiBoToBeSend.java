package com.ridding.meta;

import com.ridding.constant.SourceType;

public class WeiBoToBeSend {
	/**
	 * 账户Id
	 */
	private long accountId;
	/**
	 * 发送状态
	 */
	private int sendStatus;
	/**
	 * 上次更新时间
	 */
	private long lastUpdateTime;
	/**
	 * 源类型
	 */
	private int sourceType;

	/**
	 * 微博创建时间
	 * 
	 */
	private long createTime;

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public enum SendStatus {
		/**
		 * 尚未发送
		 */
		NotSend {
			public int getValue() {
				return 1;
			}
		},
		/**
		 * 已经发送
		 */
		Sended {
			public int getValue() {
				return 2;
			}
		};

		public abstract int getValue();

		public static SendStatus genSendStatus(int t) {
			for (SendStatus status : SendStatus.values()) {
				if (status.getValue() == t)
					return status;
			}
			return null;
		}

	}
}
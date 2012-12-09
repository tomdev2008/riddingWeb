package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-8 下午06:41:16 Class Description
 */
public class RiddingAction {

	private long id;
	private long userId;
	private long riddingId;
	private int type;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 最近更新时间
	 */
	private long lastUpdateTime;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 *骑行操作
	 * 
	 * @author apple
	 * 
	 */
	public enum RiddingActions {
		/**
		 * 喜欢(0)
		 */
		Like {
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 关注(1)
		 */
		Care {
			public int getValue() {
				return 1;
			}
		},
		/**
		 * 完成(2)
		 */
		Finished {
			public int getValue() {
				return 2;
			}
		},
		/**
		 * 使用(3)
		 */
		Use {
			public int getValue() {
				return 3;
			}
		};
		public abstract int getValue();

		public static RiddingActions genRiddingAction(int t) {
			for (RiddingActions action : RiddingActions.values()) {
				if (action.getValue() == t)
					return action;
			}
			return null;
		}
	}
}

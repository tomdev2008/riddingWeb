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
	private long objectId;

	/**
	 * 用户喜欢了
	 */
	private boolean userLiked;
	/**
	 * 用户关注了
	 */
	private boolean userCared;
	/**
	 * 用户使用了
	 */
	private boolean userUsed;
	/**
	 * 喜欢了照片
	 */
	private boolean pictureLiked;
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
	 * 骑行操作
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
		},
		/**
		 * 喜欢了某张图(4)
		 */
		LikePicture {
			public int getValue() {
				return 4;
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

	/**
	 * 骑行操作返回
	 * 
	 * @author apple
	 * 
	 */
	public enum RiddingActionResponse {
		/**
		 * 重复了(0)
		 */
		DoubleDo {
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 自己的(1)
		 */
		InRidding {
			public int getValue() {
				return 1;
			}
		},
		/**
		 * 成功(2)
		 */
		SUCC {
			public int getValue() {
				return 2;
			}
		},
		/**
		 * 失败(3)
		 */
		Fail {
			public int getValue() {
				return 3;
			}
		};
		public abstract int getValue();

		public static RiddingActionResponse genRiddingActionResponse(int t) {
			for (RiddingActionResponse response : RiddingActionResponse
					.values()) {
				if (response.getValue() == t)
					return response;
			}
			return null;
		}
	}

	public boolean isUserLiked() {
		return userLiked;
	}

	public void setUserLiked(boolean userLiked) {
		this.userLiked = userLiked;
	}

	public boolean isUserCared() {
		return userCared;
	}

	public void setUserCared(boolean userCared) {
		this.userCared = userCared;
	}

	public boolean isUserUsed() {
		return userUsed;
	}

	public void setUserUsed(boolean userUsed) {
		this.userUsed = userUsed;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public boolean getPictureLiked() {
		return pictureLiked;
	}

	public void setPictureLiked(boolean pictureLiked) {
		this.pictureLiked = pictureLiked;
	}

}

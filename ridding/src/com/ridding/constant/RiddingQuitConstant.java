package com.ridding.constant;


/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-2 下午10:31:03
 * Class Description
 */
public enum RiddingQuitConstant {
	/**
	 * 是队长，队员还有，不能退出 (0)
	 */
	Leader {
		public int getValue() {
			return 0;
		}
	},
	/**
	 * 退出失败(1)
	 */
	Failed {
		public int getValue() {
			return 1;
		}
	},
	/**
	 * 已经完成 (2)
	 */
	Success {
		public int getValue() {
			return 2;
		}
	};
	public abstract int getValue();

	public static RiddingQuitConstant genRiddingQuitConstant(int t) {
		for (RiddingQuitConstant status : RiddingQuitConstant.values()) {
			if (status.getValue() == t)
				return status;
		}
		return null;
	}
}

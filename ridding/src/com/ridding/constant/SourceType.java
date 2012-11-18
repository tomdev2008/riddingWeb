package com.ridding.constant;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-11 下午11:52:31 Class Description 外部资源类型
 */
public enum SourceType {
	/**
	 * 新浪微博 (1)
	 */
	SINAWEIBO {
		public int getValue() {
			return 1;
		}
	},
	/**
	 * 来自内部web系统
	 */
	Web {
		public int getValue() {
			return 2;
		}

	},
	/**
	 * 来自内部web系统
	 */
	WebApi {
		public int getValue() {
			return 3;
		}

	};
	public abstract int getValue();

	public static SourceType genSourceType(int t) {
		for (SourceType type : SourceType.values()) {
			if (type.getValue() == t)
				return type;
		}
		return null;
	}

}

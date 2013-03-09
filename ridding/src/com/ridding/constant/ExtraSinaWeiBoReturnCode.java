package com.ridding.constant;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime时间2012-4-15 年12:32:05 Class Description
 */
public enum ExtraSinaWeiBoReturnCode {
	/**
	 * 地址无法连接(0)
	 */
	UrlCanNotConnect {
		@Override
		public int getValue() {
			return 0;
		}
	},
	/**
	 * 插入地址错误，连接有问题(1)
	 */
	InsertError {
		@Override
		public int getValue() {
			return 1;
		}
	},
	/**
	 * 失败(2)
	 */
	Failed {
		@Override
		public int getValue() {
			return 2;
		}
	},
	/**
	 * 成功(4)
	 */
	Succeed {
		@Override
		public int getValue() {
			return 3;
		}
	};
	public abstract int getValue();

}

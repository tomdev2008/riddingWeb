package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-3-3 上午02:03:28 Class Description
 */
public class UserPay {
	/**
	 * id
	 */
	private long id;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 类型id
	 */
	private int typeId;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 开始时间
	 */
	private long beginTime;
	/**
	 * 当前状态
	 */
	private int status;
	/**
	 * 时间长度
	 */
	private int dayLong;
	/**
	 * 无效状态
	 */
	public static int status_invalid = 0;
	/**
	 * 试用期
	 */
	public static int status_trying = 1;
	/**
	 * 有效状态
	 */
	public static int status_valid = 2;
	/**
	 * 开始日期
	 */
	private String beginTimeStr;
	/**
	 * 结束日期
	 */
	private String endTimeStr;

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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDayLong() {
		return dayLong;
	}

	public void setDayLong(int dayLong) {
		this.dayLong = dayLong;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	/**
	 * 用户付费的功能
	 * 
	 * @author apple
	 * 
	 */
	public enum UserPayType {
		/**
		 * 什么都不是
		 */
		None {
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 天气功能
		 */
		Weather {
			public int getValue() {
				return 1;
			}
		},
		/**
		 * 天气功能
		 */
		HelpUs {
			public int getValue() {
				return 2;
			}
		};
		public abstract int getValue();

		public static UserPayType genUserPayType(int t) {
			for (UserPayType type : UserPayType.values()) {
				if (type.getValue() == t)
					return type;
			}
			return null;
		}
	}
}

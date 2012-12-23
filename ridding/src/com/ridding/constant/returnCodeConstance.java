package com.ridding.constant;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime��2012-3-21 ����02:16:17 Class Description 2开头表示骑行相关
 */
public class returnCodeConstance {
	/**
	 * 解析json错误
	 */
	public static final int INNEREXCEPTION = -100;
	/**
	 * 失败
	 */
	public static final int FAILED = -1;
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 不是骑行中的用户
	 */
	public static final int NOTRIDINGUSER = -302;

	/**
	 * 不是骑行中的队长
	 */
	public static final int NOTRIDINGLEADER = -202;
	/**
	 * 是队长，还有队员，不能退出
	 */
	public static final int RIDDINGLEADER = -300;
	/**
	 * 骑行活动不存在
	 */
	public static final int RIDDINGNOTEXISTED = -301;
	/**
	 * token失败
	 */
	public static final int TOKENEXPIRED = -444;
	/**
	 * 多次点击
	 */
	public static final int RiddingActionDouble = -310;
	/**
	 * 自己的骑行活动不能操作
	 */
	public static final int RiddingActionInMyRidding = -311;

}

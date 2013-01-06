package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-1-3 下午11:35:49 Class Description
 */
public class UserRelation {
	/**
	 * id
	 */
	private long id;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 对应关系的用户id
	 */
	private long toUserId;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 操作类型
	 */
	private int type;

	/**
	 * 无效
	 */
	public static int notValid = 0;
	/**
	 * 有效
	 */
	public static int Valid = 1;

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

	public long getToUserId() {
		return toUserId;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

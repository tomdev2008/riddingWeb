package com.ridding.meta;

import java.io.Serializable;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-30 03:58:45 Class Description 新浪微博信息
 */
public class Source implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3267778350045336929L;
	/**
	 * 自增长id
	 */
	private long id;
	/**
	 * 资源 id
	 */
	private long sourceId;
	/**
	 * 新浪微博内容
	 */
	private String text;
	/**
	 * sina的用户id
	 */
	private long accessUserId;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 当前新浪微博处理状态
	 */
	private int status;
	/**
	 * 资源类型(SourceType)
	 */
	private int sourceType;
	/**
	 * 创建时间string
	 */
	private String createTimeString;
	/**
	 * 用户名,不存数据库
	 */
	private String userName;

	/**
	 * 链接没有处理
	 */
	public static int notDeal = 0;
	/**
	 * 链接已经处理了
	 */
	public static int Dealed = 1;
	/**
	 * url是错的
	 */
	public static int UrlError = 2;
	/**
	 *连接无效
	 */
	public static int Invaild = 10;

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	public long getAccessUserId() {
		return accessUserId;
	}

	public void setAccessUserId(long accessUserId) {
		this.accessUserId = accessUserId;
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

	public String getCreateTimeString() {
		return createTimeString;
	}

	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}

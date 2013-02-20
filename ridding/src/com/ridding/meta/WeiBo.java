package com.ridding.meta;

import java.util.Date;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-10-22 下午11:56:48 Class Description 发送微博类
 */
public class WeiBo {
	/**
	 * id
	 */
	private long id;
	/**
	 * 文本
	 */
	private String text;
	/**
	 * 图片url
	 */
	private String photoUrl;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 发送时间
	 */
	private long sendTime;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 资源来源
	 */
	private int sourceType;
	/**
	 * 发送的微博id
	 */
	private long weiboId;
	/**
	 * 发送微博的类型，0表示默认，1表示地图微博
	 */
	private int weiboType;
	/**
	 * 活动id
	 */
	private long riddingId;
	/**
	 * 发送时间的字符串形式
	 */
	private String sendTimeStr;

	/**
	 * 没有处理
	 */
	public static int notDeal = 0;
	/**
	 * 已经处理了
	 */
	public static int Dealed = 1;

	/**
	 * weibotype
	 * 
	 * @author apple
	 * 
	 */
	public enum WeiBoType {
		/**
		 * 常规微博 (1)
		 */
		DEFAULT {
			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 地图微博
		 */
		RIDDING {
			@Override
			public int getValue() {
				return 1;
			}
		};
		public abstract int getValue();

		public static WeiBoType genWeiBoType(int t) {
			for (WeiBoType type : WeiBoType.values()) {
				if (type.getValue() == t)
					return type;
			}
			return null;
		}

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.setSendTimeStr(new Date(sendTime).toString());
		this.sendTime = sendTime;
	}

	public String getSendTimeStr() {
		return sendTimeStr;
	}

	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}

	public long getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(long weiboId) {
		this.weiboId = weiboId;
	}

	public int getWeiboType() {
		return weiboType;
	}

	public void setWeiboType(int weiboType) {
		this.weiboType = weiboType;
	}

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
	}

	

}

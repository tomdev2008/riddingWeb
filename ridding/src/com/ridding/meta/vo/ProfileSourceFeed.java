package com.ridding.meta.vo;

import java.util.List;


/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-6-14 下午10:35:12 Class Description
 */
public class ProfileSourceFeed {
	/**
	 * 用户id列表
	 */
	private List<Long> userIdList;
	/**
	 * 资源类型
	 */
	private int sourceType;

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

}

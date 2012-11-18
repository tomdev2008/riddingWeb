package com.ridding.meta.vo;

import java.util.concurrent.ConcurrentHashMap;

import com.ridding.meta.RiddingUser;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime2012-3-19 03:01:22 Class Description
 */
public class RiddingUserVO {
	/**
	 * 
	 */
	private ConcurrentHashMap<String, RiddingUser> map;
	/**
	 * 保存时间长度
	 */
	private long saveCacheTime;

	public ConcurrentHashMap<String, RiddingUser> getMap() {
		return map;
	}

	public void setMap(ConcurrentHashMap<String, RiddingUser> map) {
		this.map = map;
	}

	public long getSaveCacheTime() {
		return saveCacheTime;
	}

	public void setSaveCacheTime(long saveCacheTime) {
		this.saveCacheTime = saveCacheTime;
	}

}

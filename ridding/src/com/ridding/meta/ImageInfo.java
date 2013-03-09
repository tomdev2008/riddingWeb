package com.ridding.meta;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-2-17 上午01:16:34 Class Description
 */
public class ImageInfo {
	/**
	 * 七牛key
	 */
	private String key;
	/**
	 * url
	 */
	private String url;
	/**
	 * 宽度
	 */
	private int width;
	/**
	 * 高度
	 */
	private int height;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}

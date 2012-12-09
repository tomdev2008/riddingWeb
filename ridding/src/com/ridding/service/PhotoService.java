package com.ridding.service;

import com.ridding.meta.Photo;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-10 上午12:20:12 Class Description
 */
public interface PhotoService {
	/**
	 * 添加photo
	 * 
	 * @param photo
	 * @return
	 */
	public int addPhoto(Photo photo);

	/**
	 * 更新图片
	 * 
	 * @param photo
	 * @return
	 */
	public int updatePhoto(Photo photo);

	/**
	 * 删除图片
	 * 
	 * @param id
	 * @return
	 */
	public int deletePhoto(long id);

	/**
	 * 得到图片
	 * 
	 * @param id
	 * @return
	 */
	public Photo getPhoto(long id);
}

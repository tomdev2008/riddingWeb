package com.ridding.mapper;

import java.util.List;

import com.ridding.meta.Photo;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-10 上午12:17:14 Class Description
 */
public interface PhotoMapper {
	/**
	 * 添加photo
	 * 
	 * @param photo
	 * @return
	 */
	public int addPhoto(Photo photo);

	/**
	 * 通过id得到photo对象
	 * 
	 * @param id
	 * @return
	 */
	public Photo getPhotoById(long id);

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
	 * 得到photo列表
	 * 
	 * @param photoIdList
	 * @return
	 */
	public List<Photo> getPhotoList(List<Long> photoIdList);

}

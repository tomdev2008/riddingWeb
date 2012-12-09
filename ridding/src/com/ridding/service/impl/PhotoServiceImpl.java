package com.ridding.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.PhotoMapper;
import com.ridding.meta.Photo;
import com.ridding.service.PhotoService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-10 上午12:21:13 Class Description
 */
@Service("photoService")
public class PhotoServiceImpl implements PhotoService {
	@Resource
	private PhotoMapper photoMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PhotoService#addPhoto(com.ridding.meta.Photo)
	 */
	@Override
	public int addPhoto(Photo photo) {
		if (photo != null) {
			return photoMapper.addPhoto(photo);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PhotoService#deletePhoto(long)
	 */
	@Override
	public int deletePhoto(long id) {
		return photoMapper.deletePhoto(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PhotoService#updatePhoto(com.ridding.meta.Photo)
	 */
	@Override
	public int updatePhoto(Photo photo) {
		if (photo == null) {
			return -1;
		}
		return photoMapper.updatePhoto(photo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PhotoService#getPhoto(long)
	 */
	@Override
	public Photo getPhoto(long id) {
		return photoMapper.getPhotoById(id);
	}
}

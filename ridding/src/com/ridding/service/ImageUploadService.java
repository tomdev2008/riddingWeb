package com.ridding.service;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

import com.ridding.meta.vo.Compress;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-6 下午09:58:59 Class Description
 */
public interface ImageUploadService {
	/**
	 * 压缩图片，传入文件和压缩比例
	 * 
	 * @param file
	 * @param compress
	 * @return
	 */
	public boolean imageCompressToFile(File file, Compress compress, String destPath);

	/**
	 * 保存图片
	 * 
	 * @param fileUrl
	 * @return
	 */
	public String saveImageFromUrl(String fileUrl, long photoId);

	/**
	 * 保存图片
	 * 
	 * @param fileItem
	 * @return
	 */
	public String saveImageFromFileItem(FileItem fileItem, long photoId);

	/**
	 * 保存图片到七牛
	 * 
	 * @param fileItem
	 * @return
	 */
	public String saveImageToQiniu(FileItem fileItem);

}

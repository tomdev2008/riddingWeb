package com.ridding.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiniu.qbox.auth.DigestAuthClient;
import com.qiniu.qbox.rs.PutFileRet;
import com.qiniu.qbox.rs.RSService;
import com.ridding.constant.SystemConst;
import com.ridding.meta.Photo;
import com.ridding.meta.vo.Compress;
import com.ridding.service.ImageUploadService;
import com.ridding.service.PhotoService;
import com.ridding.util.FileUtil;
import com.ridding.util.NetImageDownloadUtil;
import com.ridding.util.ThumbnailUtil;
import com.ridding.web.controller.UploadController;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-6 下午09:59:14 Class Description
 */
@Service("imageUploadService")
public class ImageUploadServiceImpl implements ImageUploadService {
	private static final Logger logger = Logger.getLogger(ImageUploadServiceImpl.class);
	private static ExecutorService threaExecutorService = Executors.newFixedThreadPool(10);

	@Resource
	private PhotoService photoService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ImageUploadService#imageCompressToFile(java.io.File,
	 * com.ridding.meta.vo.Compress, java.lang.String)
	 */
	@Override
	public boolean imageCompressToFile(File file, Compress compress, String destPath) {
		if (file == null) {
			return false;
		}

		File destFile = new File(destPath);
		return ThumbnailUtil.generateThumb(file, destFile, compress.getWidth(), compress.getHeight(), 0.85);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ImageUploadService#saveImageFromUrl(java.lang.String)
	 */
	@Override
	public String saveImageFromUrl(String fileUrl, long photoId) {
		if (fileUrl == null) {
			photoService.deletePhoto(photoId);
			return null;
		}
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		Integer day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		String distPathEnd = "/" + year + "/" + month + "/" + day;
		String name = year + "_" + month + "_" + day + "_" + date.getTime() + ".jpg";
		this.doThread(fileUrl, distPathEnd, name, photoId);
		return SystemConst.getValue("IMGPATH") + SystemConst.getValue("ORIIMGPATH") + distPathEnd;
	}

	/**
	 * 执行线程操作
	 * 
	 * @param fileUrl
	 * @param dictPath
	 */
	private void doThread(final String fileUrl, final String distPathEnd, final String name, final long photoId) {
		threaExecutorService.execute(new Thread() {
			public void run() {
				try {
					FileUtil.CreateDir(SystemConst.getValue("IMGPATH") + SystemConst.getValue("ORIIMGPATH") + distPathEnd);
					NetImageDownloadUtil.downLoadImg(fileUrl, SystemConst.getValue("IMGPATH") + SystemConst.getValue("ORIIMGPATH") + distPathEnd
							+ "/" + name);
				} catch (Exception e) {
					e.printStackTrace();
					photoService.deletePhoto(photoId);
				}
				Photo photo = new Photo();
				photo.setId(photoId);
				photo.setOriginalPath(SystemConst.getValue("ORIIMGPATH") + distPathEnd + "/" + name);
				photoService.updatePhoto(photo);
			}
		});
	}

	@Override
	public String saveImageFromFileItem(FileItem fileItem, long photoId) {
		if (fileItem == null) {
			photoService.deletePhoto(photoId);
			return null;
		}
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		Integer day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		String distPathEnd = "/" + year + "/" + month + "/" + day;
		String name = year + "_" + month + "_" + day + "_" + date.getTime() + ".jpg";
		this.doThreadByFileItem(fileItem, distPathEnd, name, photoId);
		return SystemConst.getValue("IMGPATH") + SystemConst.getValue("ORIIMGPATH") + distPathEnd;
	}

	/**
	 * 添加文件
	 * 
	 * @param fileItem
	 * @param distPathEnd
	 * @param name
	 * @param photoId
	 */
	private void doThreadByFileItem(final FileItem fileItem, final String distPathEnd, final String name, final long photoId) {
		threaExecutorService.execute(new Thread() {
			public void run() {
				try {
					FileUtil.CreateDir(SystemConst.getValue("IMGPATH") + SystemConst.getValue("ORIIMGPATH") + distPathEnd);
					fileItem.write(new File(SystemConst.getValue("IMGPATH") + SystemConst.getValue("ORIIMGPATH") + distPathEnd + "/" + name));
				} catch (Exception e) {
					e.printStackTrace();
					photoService.deletePhoto(photoId);
				}
				Photo photo = new Photo();
				photo.setId(photoId);
				photo.setOriginalPath(SystemConst.getValue("ORIIMGPATH") + distPathEnd + "/" + name);
				photoService.updatePhoto(photo);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.ImageUploadService#saveImageToQiniu(org.apache.commons
	 * .fileupload.FileItem)
	 */
	@Override
	public String saveImageToQiniu(FileItem fileItem) {
		String name = this.getFileName();
		DigestAuthClient conn = new DigestAuthClient();
		final RSService rs = new RSService(conn, SystemConst.getValue("QINIUBUCKET"));
		File file = new File("/var/tmp/file");
		try {
			fileItem.write(file);
			PutFileRet putFileRet = rs.putFileByPath(name, null, file, null);
			if (putFileRet.statusCode == 200) {
				return name;
			} else {
				logger.error("saveImageToQiniu error where statusCode=" + putFileRet.statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 得到文件名
	 * 
	 * @return
	 */
	private String getFileName() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		Integer day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		String distPathEnd = year + "/" + month + "/" + day;
		return distPathEnd + "/" + date.getTime() + ".jpg";
	}

}

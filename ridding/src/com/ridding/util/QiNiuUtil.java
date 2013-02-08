package com.ridding.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.qiniu.qbox.Config;
import com.qiniu.qbox.auth.DigestAuthClient;
import com.qiniu.qbox.rs.PutAuthRet;
import com.qiniu.qbox.rs.PutFileRet;
import com.qiniu.qbox.rs.RSClient;
import com.qiniu.qbox.rs.RSService;
import com.ridding.constant.SystemConst;
import com.ridding.web.controller.RiddingController;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-2-7 下午05:12:59 Class Description
 */
public class QiNiuUtil {

	private static final Logger logger = Logger.getLogger(RiddingController.class);

	/**
	 * 生成七牛key
	 * 
	 * @param isUrl
	 * @param isImage
	 * @return
	 */
	public static String genKey(boolean isUrl, boolean isImage) {
		if (isUrl) {
			if (isImage) {
				return "outsource/" + new Date().getTime() + "_image.jpg";
			} else {
				return "outsource/" + new Date().getTime() + "_file.jpg";
			}
		} else {
			if (isImage) {
				return "source/" + new Date().getTime() + "_image.jpg";
			} else {
				return "source/" + new Date().getTime() + "_file.jpg";
			}
		}
	}

	/**
	 * 取本地照片上传
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean uploadImageToQiniuFromLocalFile(String filePath, String key) throws Exception {
		Config.ACCESS_KEY = SystemConst.getValue("QiNiuACCESS_KEY");
		Config.SECRET_KEY = SystemConst.getValue("QiNiuSECRET_KEY");
		String bucketName = SystemConst.getValue("QiNiuBucket");
		DigestAuthClient conn = new DigestAuthClient();
		RSService rs = new RSService(conn, bucketName);
		PutAuthRet putAuthRet = rs.putAuth();
		HashMap<String, String> callbackParams = new HashMap<String, String>();
		callbackParams.put("key", key);
		PutFileRet putFileRet = RSClient.putFile(putAuthRet.getUrl(), bucketName, key, "", filePath, "CustomData", callbackParams);
		if (!putFileRet.ok()) {
			logger.error("uploadImageToQiniuFromLocalFile error where filePath=" + filePath + " key=" + key);
			return false;
		}
		return true;
	}

	/**
	 * 通过url上传照片到七牛
	 * 
	 * @param url
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean uploadImageToQiniuFromUrl(String url, String key) throws Exception {
		String fileName = "" + new Date().getTime();
		File file = FileUtil.getFileFromUrl(url, SystemConst.getValue("UploadTEMPPATH") + fileName);
		if (file == null) {
			return false;
		}
		try {
			return QiNiuUtil.uploadImageToQiniuFromLocalFile(SystemConst.getValue("UploadTEMPPATH") + fileName, key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file.isFile()) {
				file.delete();
			}
		}
		return false;

	}
}

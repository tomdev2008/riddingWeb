package com.ridding.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-5 下午07:31:43 Class Description
 */
public class SystemConst {

	private static final Logger log = Logger.getLogger(SystemConst.class);

	private static Properties p = new Properties();

	private static Map<String, String> constMap = new HashMap<String, String>();

	private static SystemConst systemConst = null;

	public static String getValue(String key) {
		if (systemConst == null) {
			systemConst = new SystemConst();
		}
		return constMap.get(key);
	}

	static {
		String filePath = SystemConst.class.getClassLoader().getResource("/").getPath();
		File file = new File(filePath + "local/SystemConst.properties");
		if (!file.exists()) {
			file = new File(filePath + "deploy/SystemConst.properties");
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.error("获取文件输入流失败!", e);
		}
		try {
			p.load(fis);
			constMap.put("WEBAPPKEY", p.getProperty("WEBAPPKEY"));
			constMap.put("APPKEY", p.getProperty("APPKEY"));
			constMap.put("HOST", p.getProperty("HOST"));
			constMap.put("IMGPATH", p.getProperty("IMGPATH"));
			constMap.put("ORIIMGPATH", p.getProperty("ORIIMGPATH"));
			constMap.put("QINIUBUCKET", p.getProperty("QINIUBUCKET"));
			constMap.put("IMAGEHOST", p.getProperty("IMAGEHOST"));
			constMap.put("ADMINUSERSINAID", p.getProperty("ADMINUSERSINAID"));
			constMap.put("SINAHOST", p.getProperty("SINAHOST"));
		} catch (IOException e) {
			log.error("读取常量文件错误!", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("关闭输入流失败!", e);
				}
			}
		}
	}

	/**
	 * 得到图片地址
	 * 
	 * @param key
	 * @return
	 */
	public static String returnPhotoUrl(String key) {
		return SystemConst.getValue("IMAGEHOST") + key;
	}
}

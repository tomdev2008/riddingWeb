package com.ridding.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-11 下午02:50:00 Class Description
 */
public class FileUtil {

	private static final String TEMPPATH = "/var/tmp/fileonline.jpg";

	/**
	 * 创建目录
	 * 
	 * @param distPath
	 */
	public static void CreateDir(String distPath) throws Exception {
		File file = new File(distPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * url转file
	 * 
	 * @param urlStr
	 * @return
	 */
	public static File getFileFromUrl(String urlStr) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			DataInputStream in = new DataInputStream(connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(TEMPPATH));
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			return new File(TEMPPATH);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

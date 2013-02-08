package com.ridding.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.qiniu.qbox.auth.DigestAuthClient;
import com.qiniu.qbox.rs.RSService;
import com.ridding.util.QiNiuUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-6 下午08:46:33 Class Description
 */
@Controller("uploadController")
public class UploadController extends AbstractBaseController {
	private static final Logger logger = Logger.getLogger(UploadController.class);

	private static final String TEMPPATH = "/Users/apple/Desktop/";

	/**
	 * 图片上传接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView authUpload(HttpServletRequest request, HttpServletResponse response) throws Exception, IOException {
		RequestContext requestContext = new ServletRequestContext(request);
		ModelAndView mv = new ModelAndView("upload");
		if (FileUpload.isMultipartContent(requestContext) && request.getMethod().toLowerCase().equals("post")) {
			String bucketName = "bucketName";
			DigestAuthClient conn = new DigestAuthClient();
			RSService rs = new RSService(conn, bucketName);

			// 通过该实例化的资源表对象来进行文件上传

			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(2000000);
			List items = new ArrayList();
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e1) {
				logger.error("文件上传发生错误" + e1.getMessage());
			}
			Iterator it = items.iterator();
			while (it.hasNext()) {
				FileItem fileItem = (FileItem) it.next();
				if (fileItem.isFormField()) {
					logger.error("");
				} else {
					if (fileItem.getName() != null && fileItem.getSize() != 0) {
						String name = "" + new Date().getTime() + ".jpg";
						File file = new File(TEMPPATH + name);
						fileItem.write(file);
						String key = QiNiuUtil.genKey(false, true);
						boolean succ = QiNiuUtil.uploadImageToQiniuFromLocalFile(TEMPPATH + name, key);
						if (succ) {
							mv.addObject("imageUrl", "/" + key);
						}
						if (file.isFile()) {
							file.delete();
						}
					} else {
						logger.error("文件没有选择 或 文件内容为空");
					}
				}
			}
		}
		return mv;
	}
}

package com.ridding.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.ridding.service.PhotoService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-6 下午08:46:33 Class Description
 */
@Controller("uploadController")
public class UploadController extends AbstractBaseController {
	private static final Logger logger = Logger.getLogger(UploadController.class);
	@Resource
	private PhotoService photoService;

	/**
	 * 图片上传接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView authUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestContext requestContext = new ServletRequestContext(request);
		ModelAndView mv = new ModelAndView("upload");
		if (FileUpload.isMultipartContent(requestContext) && request.getMethod().toLowerCase().equals("post")) {
//			DiskFileItemFactory factory = new DiskFileItemFactory();
//			ServletFileUpload upload = new ServletFileUpload(factory);
//			upload.setSizeMax(2000000);
//			List items = new ArrayList();
//			try {
//				items = upload.parseRequest(request);
//			} catch (FileUploadException e1) {
//				logger.error("文件上传发生错误" + e1.getMessage());
//			}
//			Iterator it = items.iterator();
//			while (it.hasNext()) {
//				FileItem fileItem = (FileItem) it.next();
//				if (fileItem.isFormField()) {
//					logger.error("");
//				} else {
//					if (fileItem.getName() != null && fileItem.getSize() != 0) {
//						String name = "/" + imageUploadService.saveImageToQiniu(fileItem);
//						if (name != null) {
//							Photo photo = new Photo();
//							photo.setOriginalPath(name);
//							photoService.addPhoto(photo);
//							mv.addObject("imageUrl", name);
//							return mv;
//						}
//					} else {
//						logger.error("文件没有选择 或 文件内容为空");
//					}
//				}
//			}
		}
		return mv;
	}

}

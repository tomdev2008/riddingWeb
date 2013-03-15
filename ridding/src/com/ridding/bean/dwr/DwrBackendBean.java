package com.ridding.bean.dwr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.transaction.TransactionException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ridding.constant.SystemConst;

import com.ridding.meta.Feedback;
import com.ridding.meta.IMap;
import com.ridding.meta.ImageInfo;
import com.ridding.meta.Public;
import com.ridding.meta.Ridding;
import com.ridding.meta.RiddingPicture;
import com.ridding.meta.Source;
import com.ridding.meta.WeiBo;
import com.ridding.meta.Public.PublicContentType;
import com.ridding.meta.Public.PublicType;
import com.ridding.security.MyUser;
import com.ridding.service.FeedbackService;
import com.ridding.service.IOSApnsService;
import com.ridding.service.PublicService;
import com.ridding.service.RiddingCommentService;
import com.ridding.service.RiddingService;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.SourceService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.QiNiuUtil;
import com.ridding.util.UrlUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-4-15 05:04:08 Class Description
 */
@Service("dwrBackendBean")
public class DwrBackendBean {

	private static final Logger logger = Logger.getLogger(DwrBackendBean.class);
	@Resource
	private SourceService sourceService;

	@Resource
	private TransactionService transactionService;

	@Resource
	private SinaWeiBoService sinaWeiBoService;
	@Resource
	private IOSApnsService iosApnsService;
	@Resource
	private PublicService publicService;
	@Resource
	private RiddingService riddingService;

	@Resource
	private RiddingCommentService riddingCommentService;

	@Resource
	private FeedbackService feedbackService;

	/**
	 * 更新非法的新浪微博
	 */
	public boolean updateInvalidedSource(long sourceId, int sourceType) {
		return sourceService.updateSource(sourceId, Source.Invaild, sourceType);
	}

	/**
	 * 更新新的地图插入
	 * 
	 * @param sinaWeiBoId
	 * @param input
	 * @return
	 */
	public boolean updateNewMapInput(long sourceId, String input, int sourceType) {
		Source source = sourceService.getSourceBySourceId(sourceId, sourceType);
		if (source == null) {
			return false;
		}
		if (!sourceService.updateSource(sourceId, Source.Dealed, sourceType)) {
			return false;
		}
		IMap iMap = new IMap();
		iMap.setMapUrl(input);
		iMap.setObjectId(source.getSourceId());
		iMap.setObjectType(source.getSourceType());
		long nowTime = new Date().getTime();
		iMap.setCreateTime(nowTime);
		try {
			return transactionService.insertMap(iMap, source);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 创建需要发送的微博
	 * 
	 * @param text
	 * @param date
	 * @param photoUrl
	 * @param sourceType
	 * @return
	 */
	public boolean updateWeiBo(String text, String date, String photoUrl,
			int sourceType, int weiboType, long riddingId) {
		WeiBo weiBo = new WeiBo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date adate;
		try {
			adate = sdf.parse(date);
			long sendTime = adate.getTime();
			weiBo.setSendTime(sendTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		weiBo.setStatus(WeiBo.notDeal);
		weiBo.setText(text);
		weiBo.setWeiboType(weiboType);
		weiBo.setPhotoUrl(photoUrl);
		weiBo.setSourceType(Integer.valueOf(sourceType));
		weiBo.setCreateTime(new Date().getTime());
		weiBo.setRiddingId(riddingId);
		return sinaWeiBoService.addWeiBo(weiBo);
	}

	/**
	 * 发送apns消息
	 * 
	 * @param text
	 * @return
	 */
	public void sendApns(String text, long userId, String version) {
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder
				.getContext().getAuthentication()).getDetails();
		if (myUser.getUserId() == 54) {
			
			iosApnsService.sendApns(text, userId, version);
		}else{
			logger.error("dwrbackendbean sendapns error where not admin");
		}
	}

	/**
	 * 添加推荐
	 * 
	 * @param riddingId
	 * @param userId
	 */
	public boolean addPublicRecom(long riddingId, int weight,
			String firstPicUrl, String linkText, String linkImageUrl,
			String linkUrl) {
		Public public1 = new Public();
		public1.setRiddingId(riddingId);
		public1.setFirstPicUrl(firstPicUrl);
		if (StringUtils.isEmpty(linkText) && StringUtils.isEmpty(linkImageUrl)) {
			public1.setAdContentType(PublicContentType.PublicNone.getValue());
		} else if (StringUtils.isEmpty(linkText)) {
			public1.setAdContentType(PublicContentType.PublicImage.getValue());
			public1.setAdImageUrl(linkImageUrl);
			public1.setLinkUrl(linkUrl);
		} else {
			public1.setAdContentType(PublicContentType.PublicText.getValue());
			public1.setAdText(linkText);
			public1.setLinkUrl(linkUrl);
		}
		public1.genJson();
		public1.setWeight(weight);
		public1.setType(PublicType.PublicRecom.getValue());
		return publicService.addPublic(public1);
	}

	/**
	 * 删除骑行评论
	 * 
	 * @param riddingId
	 * @param commentId
	 * @return
	 */
	public void deleteRiddingComment(long commentId) {
		if (commentId < 0) {
			return;
		}
		// warning need auth
		riddingCommentService.deleteRiddingCommentByReplyIdAndCount(commentId);
	}

	/**
	 * 设置为推荐
	 * 
	 * @param riddingId
	 * @return
	 */
	public boolean setIsRecom(long riddingId) {
		if (riddingId < 0) {
			return false;
		}
		return riddingService.setRiddingIsRecom(riddingId);
	}

	/**
	 * 取消推荐
	 * 
	 * @param riddingId
	 * @return
	 */
	public boolean setIsNotRecom(long publicId, long riddingId) {
		if (publicId < 0) {
			return false;
		}
		return riddingService.setRiddingIsNotRecom(publicId, riddingId);
	}

	/**
	 * 删除骑行活动,包括骑行队员，骑行图片，骑行评论，骑行操作
	 * 
	 * @param riddingId
	 * @return
	 **/
	public boolean deleteRiddingById(long riddingId) {
		if (riddingId < 0) {
			return false;
		}
		try {
			return transactionService.deleteRiddingAndLinkedThings(riddingId);
		} catch (TransactionException e) {
			logger.error("报错了呗~");
			return false;
		}
	}

	/**
	 * 更新推荐头图
	 * 
	 * @param id
	 * @param picUrl
	 * @return
	 */
	public boolean updatePublicFirstPicUrl(long id, String picUrl) {
		return publicService.updatePublicFirstPicUrl(id, picUrl);
	}

	/**
	 * 添加骑行照片
	 * 
	 * @param riddingId
	 * @param url
	 * @param desc
	 * @param takePicDate
	 * @return
	 */
	public boolean addRiddingPicture(long riddingId, String url, String desc,
			String takePicDate, String takePicLocation, long breadId) {
		Ridding ridding = riddingService.getRidding(riddingId);
		if (ridding == null) {
			return false;
		}
		if (url.startsWith("http")) {
			String key = QiNiuUtil.genKey(true, true);
			try {
				boolean succ = QiNiuUtil.uploadImageToQiniuFromUrl(url, key);
				if (succ) {
					logger.info("QiNiuUtil.uploadImageToQiniuFromUrl succ where fromUrl="
							+ url + " and key=" + key);
					url = "/" + key;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		RiddingPicture riddingPicture = new RiddingPicture();
		riddingPicture.setUserId(ridding.getLeaderUserId());

		ImageInfo imageInfo = QiNiuUtil.getImageInfoFromQiniu(SystemConst
				.returnPhotoUrl(url));
		if (imageInfo == null) {
			logger.error("imageInfo is null where url=" + url);
		}
		riddingPicture.setWidth(imageInfo.getWidth());
		riddingPicture.setHeight(imageInfo.getHeight());
		riddingPicture.setRiddingId(riddingId);
		riddingPicture.setPhotoUrl(url);
		try {
			riddingPicture.setDescription(desc);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long nowTime = new Date().getTime();
		riddingPicture.setCreateTime(nowTime);
		riddingPicture.setLastUpdateTime(nowTime);
		riddingPicture.setBreadId(breadId);
		long sendTime = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date adate = sdf.parse(takePicDate);
			sendTime = adate.getTime();
		} catch (ParseException e) {

			try {
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM月dd日 HH:mm");
				Date adate = sdf.parse(year + takePicDate);
				sendTime = adate.getTime();
			} catch (ParseException e1) {
				logger.error("addRiddingPicture date error where str="
						+ takePicDate);
				e1.printStackTrace();
				return false;
			}
		}

		riddingPicture.setTakePicDate(sendTime);
		riddingPicture.setTakePicLocation(takePicLocation);

		return riddingService.addRiddingPicture(riddingPicture);

	}

	/**
	 * 通过面包添加骑行照片
	 * 
	 * @param riddingId
	 * @param mianbaoUrl
	 * @return
	 */
	public boolean getRiddingPictureFromMianBao(long riddingId,
			String mianbaoUrl) {
		StringBuffer sbBuffer = UrlUtil.getSourceCodeFromUrl(mianbaoUrl);
		if (sbBuffer == null) {
			return false;
		}
		Document doc = Jsoup.parse(sbBuffer.toString());
		Elements contents = doc.getElementsByClass("waypoint");
		if (!contents.isEmpty()) {
			for (Element element : contents) {
				String imageUrl = null;
				String content = null;
				String dateStr = null;
				String locationStr = null;

				Long breadId = Long.valueOf(element.attr("data-waypoint_id")
						.toString());

				RiddingPicture picture = riddingService
						.getRiddingPictureByBreadId(breadId, riddingId);
				if (picture != null) {
					continue;
				}
				Elements imageElements = element
						.getElementsByClass("photo-ctn");
				if (imageElements.isEmpty()) {
					continue;
				}
				for (Element image : imageElements) {
					Element links = image.getElementsByTag("a").first();
					imageUrl = links.attr("href");
					logger.info(links.attr("href"));
				}
				Elements contentElements = element
						.getElementsByClass("photo-comment");
				logger.info(contentElements.text());
				content = contentElements.text();

				Elements dateElements = element.getElementsByClass("time");
				logger.info(dateElements.text());
				dateStr = dateElements.text();

				Elements locationElements = element
						.getElementsByClass("ellipsis_text");
				logger.info(locationElements.text());
				locationStr = locationElements.text();

				this.addRiddingPicture(riddingId, imageUrl, content, dateStr,
						locationStr, breadId);
			}
		}
		return true;
	}


	/**
	 * 回复反馈
	 * 
	 * @param id
	 * @param userId
	 * @param reply
	 * @return
	 */
	public boolean replyFeedback(long id, long userId, String reply) {
		return feedbackService.replyFeedback(id, userId, reply);
	}

	/**
	 * 更新广告内容
	 * 
	 * @param publicId
	 * @param pictureId
	 * @return
	 */
	public boolean setFirstPicUrl(long publicId, long pictureId) {
		if (publicId < 0 || pictureId < 0) {
			return false;
		}
		RiddingPicture riddingPicture = riddingService
				.getRiddingPictureById(pictureId);
		if (riddingPicture == null) {
			return false;
		}

		Public public1 = publicService.getPublicById(publicId);
		if (public1 == null) {
			return false;
		}
		public1.getJson();
		public1.setFirstPicUrl(riddingPicture.getPhotoUrl());
		public1.genJson();
		return publicService.updatePublic(publicId, public1.getJson());
	}

	/**
	 * 通过Id删除广告
	 * 
	 * @param publicId
	 * @return
	 */
	public boolean deletePublicByPublicId(long publicId) {
		if (publicId < 0) {
			return false;
		}
		return publicService.deletePublicByPublicId(publicId);
	}
}

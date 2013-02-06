package com.ridding.bean.dwr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.transaction.TransactionException;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ridding.mapper.MapFixMapper;
import com.ridding.mapper.RiddingCommentMapper;
import com.ridding.mapper.RiddingMapper;
import com.ridding.meta.IMap;
import com.ridding.meta.Public;
import com.ridding.meta.Source;
import com.ridding.meta.WeiBo;
import com.ridding.meta.Public.PublicContentType;
import com.ridding.meta.Public.PublicType;
import com.ridding.security.MyUser;
import com.ridding.service.IOSApnsService;
import com.ridding.service.MapService;
import com.ridding.service.PublicService;
import com.ridding.service.RiddingCommentService;
import com.ridding.service.RiddingService;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.SourceService;
import com.ridding.service.transaction.TransactionService;

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
	private MapService mapService;

	@Resource
	private RiddingCommentService riddingCommentService;

	@Resource
	private RiddingCommentMapper riddingCommentMapper;

	@Resource
	private MapFixMapper mapFixMapper;

	@Resource
	private RiddingMapper riddingMapper;

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
	public boolean updateWeiBo(String text, String date, String photoUrl, int sourceType, int weiboType, long riddingId) {
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
	public void sendApns(String text) {
		MyUser myUser = (MyUser) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getDetails();
		if (myUser.getUserId() == 54) {
			iosApnsService.sendApns(text);
		}
	}

	/**
	 * 添加推荐
	 * 
	 * @param riddingId
	 * @param userId
	 */
	public boolean addPublicRecom(long riddingId, int weight, String firstPicUrl, String linkText, String linkImageUrl, String linkUrl) {
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
	 * 更新firstPic
	 * 
	 * @param id
	 * @param picUrl
	 * @return
	 */
	public boolean updatePublicFirstPicUrl(long id, String picUrl) {
		return publicService.updatePublicFirstPicUrl(id, picUrl);
	}
}

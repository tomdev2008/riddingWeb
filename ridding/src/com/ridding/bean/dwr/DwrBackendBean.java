package com.ridding.bean.dwr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ridding.meta.IMap;
import com.ridding.meta.Source;
import com.ridding.meta.WeiBo;
import com.ridding.meta.Public.PublicType;
import com.ridding.security.MyUser;
import com.ridding.service.IOSApnsService;
import com.ridding.service.MapService;
import com.ridding.service.PhotoService;
import com.ridding.service.PublicService;
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
	private PhotoService photoService;

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
	 * 发送apns小新
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
	public boolean addPublicRecom(long riddingId, long userId, int weight, String firstPicUrl) {
//		if (StringUtils.isEmpty(firstPicUrl)) {
//			Ridding ridding = riddingService.getRidding(riddingId);
//			if (ridding != null) {
//				List<RiddingPicture> list = riddingService.getRiddingPictureByRiddingId(riddingId, 1, new Date().getTime());
//				if (!ListUtils.isEmptyList(list)) {
//					RiddingPicture picture = list.get(0);
//					firstPicUrl = picture.getPhotoUrl();
//				} else {
//					IMap iMap = mapService.getMapById(ridding.getMapId(), IMap.Using);
//					if (iMap != null) {
//						Photo photo = photoService.getPhoto(iMap.getAvatorPic());
//						if (photo != null) {
//							firstPicUrl = photo.getOriginalPath();
//						}
//					}
//				}
//
//			}
//		}
		String json = PublicType.PublicRecom.setJson(userId, riddingId, firstPicUrl);

		return publicService.addPublic(PublicType.PublicRecom.getValue(), json, weight);
	}

}

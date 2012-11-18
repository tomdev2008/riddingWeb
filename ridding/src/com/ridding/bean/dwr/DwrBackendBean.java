package com.ridding.bean.dwr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.meta.IMap;
import com.ridding.meta.Source;
import com.ridding.meta.WeiBo;
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
}

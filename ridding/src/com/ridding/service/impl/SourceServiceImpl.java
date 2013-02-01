package com.ridding.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.constant.ExtraSourceReturnCode;
import com.ridding.constant.SourceType;
import com.ridding.mapper.SourceMapper;
import com.ridding.meta.IMap;
import com.ridding.meta.Ridding;
import com.ridding.meta.Source;
import com.ridding.meta.SourceAccount;
import com.ridding.service.MapService;
import com.ridding.service.ProfileService;
import com.ridding.service.RiddingService;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.SourceService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;
import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-12 上午12:13:17 Class Description
 */
@Service("sourceService")
public class SourceServiceImpl implements SourceService {

	private static final Logger logger = Logger.getLogger(SourceServiceImpl.class);
	@Resource
	private SourceMapper sourceMapper;

	@Resource
	private RiddingService riddingService;

	@Resource
	private MapService mapService;

	@Resource
	private TransactionService transactionService;

	@Resource
	private ProfileService profileService;

	@Resource
	private SinaWeiBoService sinaWeiBoService;

	/**
	 * 添加资源
	 * 
	 * @param sourceId
	 * @param accessUserId
	 * @param text
	 * @param sourceType
	 * @return
	 */
	public int addSource(long sourceId, long accessUserId, String text, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", sourceType);
		map.put("sourceId", sourceId);
		if (sourceMapper.getSourceBySourceId(map) != null) {
			return 0;
		}
		Source source = new Source();
		source.setSourceId(sourceId);
		source.setAccessUserId(accessUserId);
		source.setSourceType(sourceType);
		source.setText(text);
		long nowTime = new Date().getTime();
		source.setCreateTime(nowTime);
		source.setStatus(Source.notDeal);
		return sourceMapper.addSource(source);
	}

	/**
	 * 从资源中取出内容转化成地图信息
	 */
	public void getIMapListFromSourceQuartz() {
		logger.info("change Source to map or user begin!");
		int limit = 100;
		int offset = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("offset", offset);
		map.put("status", Source.notDeal);
		List<Source> sourceList = sourceMapper.getSourceListNoType(map);
		logger.info("getIMapListFromSourceQuartz getsize=" + sourceList == null ? 0 : sourceList.size());
		while (!ListUtils.isEmptyList(sourceList)) {
			offset = offset + limit;
			ExtraSourceReturnCode code;
			boolean succRidding;
			for (Source source : sourceList) {
				code = this.doInsertToIMap(source); // 插入imap操作
				// succRidding = this.doAddUserToRidding(source); // 插入用户信息
				// if (code == ExtrasourceReturnCode.Failed && !succRidding)
				// {
				if (code == ExtraSourceReturnCode.Succeed) {
					source.setStatus(Source.Dealed);
				} else if (code == ExtraSourceReturnCode.InsertError) {
					source.setStatus(Source.UrlError); // 插入连接有问题
				} else if (code == ExtraSourceReturnCode.Failed) {
					source.setStatus(Source.Invaild); // 设置为非法，无效连接
				} else {
					continue;
				}
				sourceMapper.updateSourceStatus(source);
			}
			map.put("offset", offset);
			sourceList = sourceMapper.getSourceListNoType(map);
		}
		logger.info("change source to map or user end!");
	}

	/**
	 * 插入地图
	 * 
	 * @param source
	 */
	private ExtraSourceReturnCode doInsertToIMap(Source source) {
		List<String> urlList = this.extraSourceShortUrl(source.getText());
		if (ListUtils.isEmptyList(urlList)) {
			return ExtraSourceReturnCode.Failed;
		}
		return this.doInsert(urlList, source);
	}

	/**
	 * 抽取新浪微博短连接
	 * 
	 * @param text
	 * @return
	 */
	private List<String> extraSourceShortUrl(String text) {
		// 连接符合 http://t.cn/
		String regexp = "(http://).*?(?=(@|&nbsp;|\\s|　|<br />|$|[<>]))";
		Pattern p = Pattern.compile(regexp);
		Matcher matcher = p.matcher(text);
		String mapUrl;
		List<String> urlList = new ArrayList<String>();
		while (matcher.find()) {
			mapUrl = matcher.group();
			String returnString = this.extraSourceSubShortUrl(mapUrl);
			if (returnString == null) {
				return null;
			}
			logger.info("get returnString=" + returnString);
			urlList.add(returnString);
			return urlList;
		}
		return null;
	}

	/**
	 * 抽取有重定向的连接
	 * 
	 * @param mapUrl
	 * @return
	 */
	private String extraSourceSubShortUrl(String mapUrl) {
		logger.info("extrasourceSubShortUrl mapUrl=" + mapUrl);
		URL url = null;
		HttpURLConnection conn = null;
		String returnString;
		try {
			url = new URL(mapUrl);
			conn = (HttpURLConnection) url.openConnection();
			logger.info("get check source return code=" + conn.getResponseCode());
		} catch (UnknownHostException e) {
			logger.info("doInsertToIMap UnknownHostException where weibo mapUrl=" + mapUrl);
			conn.disconnect();
			return null;
		} catch (Exception e) {
			logger.info("doInsertToIMap google url Connect exception where url=" + url.toString());
		} finally {
			returnString = conn.getURL().toString();
			logger.info("returnString =" + returnString);
			conn.disconnect();
		}
		// 如果不相等，说明还要继续重定向
		if (returnString.equals(mapUrl)) {
			logger.info("into equal where mapUrl=" + mapUrl);
			return returnString;
		}
		return this.extraSourceSubShortUrl(returnString);
	}

	/**
	 * 提取出数据
	 * 
	 * @return
	 */
	private Ridding extraRiddingToRidingId(String text) {
		String regexp = "(#骑行app#).*(路线:).*(活动号).*(添加队员).*(队员id:)";
		Pattern p = Pattern.compile(regexp);
		Matcher matcher = p.matcher(text);
		while (matcher.find()) {
			int index = text.indexOf("队员:") + 4;
			try {
				Long riddingId = Long.valueOf(text.substring(index, text.indexOf(" ", index)));
				if (riddingId == null || riddingId < 0) {
					return null;
				}
				return riddingService.getRidding(riddingId);
			} catch (Exception e) {
				logger.info("text is not Ridding ,text=" + text);
			}
		}
		return null;
	}

	/**
	 * 提取出骑行用户列表
	 * 
	 * @param text
	 * @return
	 */
	private List<Long> extraRiddingUserList(String text) {
		int index = text.indexOf("队员id:(") + 6;
		String users = text.substring(index, text.indexOf(")", index));
		String[] userArray = StringUtils.split(users, ",");
		if (ArrayUtils.isEmpty(userArray)) {
			return null;
		}
		try {
			List<Long> userList = new ArrayList<Long>(userArray.length);
			for (String user : userArray) {
				userList.add(Long.valueOf(user));
			}
			return userList;
		} catch (Exception e) {
			logger.error("extraRiddingUserList userArray=" + userArray.toString());
			return null;
		}
	}

	/**
	 * 做插入操作
	 * 
	 * @param urlList
	 */
	private ExtraSourceReturnCode doInsert(List<String> urlList, Source source) {
		long nowTime = new Date().getTime();
		boolean isSuitOneOfList = false;
		for (String url : urlList) {
			logger.info("get useful address,url=" + url + "is checking google link");
			String returnUrl = mapService.extraGoogleMapUrl(url);
			if (returnUrl != null) {
				try {
					IMap iMap = new IMap();
					iMap.setMapUrl(returnUrl);
					iMap.setObjectId(source.getSourceId());
					iMap.setObjectType(source.getSourceType());
					iMap.setCreateTime(nowTime);
					transactionService.insertMap(iMap, source);
					isSuitOneOfList = true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("insertMap error!" + e.getMessage());
					continue;
				}
			}
		}
		if (!isSuitOneOfList) {
			logger.info("extra google address failed that is not google link,url=" + urlList.toString());
			return ExtraSourceReturnCode.InsertError;
		}
		return ExtraSourceReturnCode.Succeed;
	}


	public List<Source> getSourceListBystatus(int status, int limit, int offset, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("offset", offset);
		map.put("status", status);
		map.put("sourceType", sourceType);
		return sourceMapper.getSourceListNoType(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.SinaWeiBoService#updateInvalidedSinaWeiBo(java.lang
	 * .Long[])
	 */
	public boolean updateSource(long sourceId, int status, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceId", sourceId);
		map.put("sourceType", sourceType);
		map.put("status", status);
		Source source = sourceMapper.getSourceBySourceId(map);
		if (source != null) {
			source.setStatus(status);
			return sourceMapper.updateSourceStatus(source) > 0;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SourceService#getSourceCountByStatus(int, int)
	 */
	public int getSourceCountByStatus(int status, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", sourceType);
		map.put("status", status);
		return sourceMapper.getSourceCountByStatus(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SourceService#getSourceListWithAccount(int, int,
	 * int, int)
	 */
	public List<Source> getSourceListWithAccount(int status, int limit, int offset, int sourceType) {
		List<Source> sourceList = this.getSourceListBystatus(status, limit, offset, sourceType);
		if (!ListUtils.isEmptyList(sourceList)) {
			List<Long> userIdList = new ArrayList<Long>(sourceList.size());
			for (Source source : sourceList) {
				userIdList.add(source.getAccessUserId());
			}
			List<SourceAccount> accounts = profileService.getAccountBySourceUserIds(userIdList, SourceType.SINAWEIBO.getValue());
			Map<Long, SourceAccount> accountMap = HashMapMaker.listToMap(accounts, "getAccessUserId", SourceAccount.class);
			for (Source source : sourceList) {
				SourceAccount account = accountMap.get(source.getAccessUserId());
				if (account != null) {
					source.setUserName(account.getAccessUserName());
				}
				source.setCreateTimeString(TimeUtil.getFormatTime(source.getCreateTime()));
			}
		}
		return sourceList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SourceService#getDealedNotInMap(int, int, int,
	 * int)
	 */
	public List<Source> getDealedNotInMap(int status, int limit, int offset, int sourceType) {
		List<Source> dealedSourceList = this.getSourceListWithAccount(Source.Dealed, limit, offset, sourceType);
		if (!ListUtils.isEmptyList(dealedSourceList)) {
			List<Long> objectIds = new ArrayList<Long>(dealedSourceList.size());
			for (Source source : dealedSourceList) {
				objectIds.add(source.getSourceId());
			}
			List<IMap> iMapList = mapService.getImapByObject(objectIds, sourceType, 0, 0);
			Map<Long, IMap> iMapMap = HashMapMaker.listToMap(iMapList, "getObjectId", IMap.class);
			List<Source> dealedNotInMapList = new ArrayList<Source>();
			for (Source source : dealedSourceList) {
				IMap map = iMapMap.get(source.getSourceId());
				if (map == null) {
					dealedNotInMapList.add(source);
				}
			}
			return dealedNotInMapList;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SourceService#getSourceBySourceId(long, int)
	 */
	public Source getSourceBySourceId(long sourceId, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", sourceType);
		map.put("sourceId", sourceId);
		return sourceMapper.getSourceBySourceId(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SourceService#getBigestId(int)
	 */
	public long getBigestId(int sourceType) {
		if (SourceType.genSourceType(sourceType) == null) {
			return -1L;
		}
		Long id = sourceMapper.getBigestId(sourceType);
		return id == null ? 0 : id;
	}

	/**
	 * 发表回复
	 * 
	 * @param objectId
	 * @param objectType
	 */
	public void sendObjectCallBack(long objectId, int objectType, String comment) {
		switch (SourceType.genSourceType(objectType)) {
		case SINAWEIBO:
			sinaWeiBoService.sendSinaWeiBoCallBack(objectId, comment);
			break;
		default:
			logger.error("sendObjectCallBack error where type=" + objectType);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SourceService#checkSourceUser(java.lang.String,
	 * long, int)
	 */
	public boolean checkSourceUser(String accessToken, long accessUserId, int sourceType) {
		switch (SourceType.genSourceType(sourceType)) {
		case SINAWEIBO:
			return sinaWeiBoService.checkSinaWeiBoUser(accessUserId, accessToken);
		default:
			logger.error("sendObjectCallBack error where type=" + sourceType);
			break;
		}
		return false;
	}

}

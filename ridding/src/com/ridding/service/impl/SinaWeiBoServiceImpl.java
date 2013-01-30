package com.ridding.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import weibo4j.Account;
import weibo4j.Comments;
import weibo4j.Weibo;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;

import com.ridding.constant.SourceType;
import com.ridding.constant.SystemConst;
import com.ridding.mapper.RepostMapWeiBoMapper;
import com.ridding.mapper.WeiBoMapper;
import com.ridding.meta.RepostMap;
import com.ridding.meta.SourceAccount;
import com.ridding.meta.WeiBo;
import com.ridding.meta.WeiBo.WeiBoType;
import com.ridding.service.ProfileService;
import com.ridding.service.SinaWeiBoService;
import com.ridding.service.SourceService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.ListUtils;
import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 04:59:49 Class Description
 */
@Service("sinaWeiBoService")
public class SinaWeiBoServiceImpl implements SinaWeiBoService {

	private static final Logger logger = Logger.getLogger(SinaWeiBoServiceImpl.class);

	private Long biggestId = null;

	private static Weibo weibo = new Weibo();

	private static String accessToken = "2.00lAUquCYpMUcE29a73d45e7qhlkMB";

	@Resource
	private SourceService sourceService;

	@Resource
	private ProfileService profileService;

	@Resource
	private WeiBoMapper weiBoMapper;
	@Resource
	private RepostMapWeiBoMapper repostMapWeiBoMapper;
	@Resource
	private TransactionService transactionService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#getAtMeSinaWeiBoQuartz()
	 */
	public void getAtMeSinaWeiBoQuartz() {
		logger.info("get @riddingapp sinaweibo begin!");
		biggestId = sourceService.getBigestId(SourceType.SINAWEIBO.getValue());
		if (biggestId == null) {
			biggestId = 0L;
		}
		SourceAccount sourceAccount = profileService.getSourceAccountByAccessUserId(Long.valueOf(SystemConst.getValue("ADMINUSERSINAID")),
				SourceType.SINAWEIBO.getValue());
		if (sourceAccount == null || StringUtils.isEmpty(sourceAccount.getAccessToken())) {
			logger.error("get @riddingapp sinaweibo error where accessToken=null");
			return;
		}
		accessToken = sourceAccount.getAccessToken();
		this.getAtMeSinaWeiBo(Long.valueOf(SystemConst.getValue("ADMINUSERSINAID")), accessToken, biggestId, 0, 0, 0, 50, 1); // 每50个取一次
		logger.info("get @riddingapp sinaweibo end!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#getAtMeSinaWeiBo(long,
	 * java.lang.String, java.lang.String, long, int, int, int)
	 */
	public void getAtMeSinaWeiBo(long userId, String accessToken, long sinceId, int filterByAuthor, int filterBySource, int filterByType, int count,
			int page) {

		StringBuilder sb = new StringBuilder(SystemConst.getValue("SINAHOST") + "/statuses/mentions.json");
		sb.append("?source=" + SystemConst.getValue("WEBAPPKEY"));
		sb.append("&uid=" + userId);
		sb.append("&access_token=" + accessToken);
		sb.append("&since_id=" + sinceId);
		sb.append("&count=" + count);
		sb.append("&page=" + page);
		sb.append("&filter_by_author=" + filterByAuthor);
		sb.append("&filter_by_source=" + filterBySource);
		sb.append("&filter_by_type=" + filterByType);
		int result = -1;
		String response = null;
		try {
			HttpClient httpclient = new HttpClient();
			GetMethod get = new GetMethod();
			get.setURI(new URI(sb.toString(), true, "utf-8"));
			result = httpclient.executeMethod(get);
			response = get.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return;
		}

		if (result == 200 && !StringUtils.isEmpty(response)) {
			this.extraSinaWeiBo(response, sinceId, page);
		} else {
			logger.error("getAtMeSinaWeiBo return code error and code=" + result + "");
		}
	}

	/**
	 * 提取出新浪微博
	 * 
	 * @param jsonString
	 * @param sinceId
	 * @param page
	 */
	private void extraSinaWeiBo(String jsonString, long sinceId, int page) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JSONArray array = (JSONArray) jsonObject.get("statuses");
		if (jsonObject == null || array == null) {
			return;
		}
		Object[] objects = array.toArray();
		if (!ArrayUtils.isEmpty(objects)) {
			boolean end = false;
			Long weiboId = null;
			JSONObject retweeted_status = null;
			logger.info("getsinaWeiBo size=" + objects.length);

			for (int index = objects.length - 1; index >= 0; index--) {
				JSONObject object2 = (JSONObject) objects[index];
				weiboId = object2.getLong("id");
				retweeted_status = (JSONObject) object2.get("retweeted_status");
				if (retweeted_status != null) {
					continue;
				}
				if (weiboId == null || (weiboId != null && weiboId <= sinceId)) {
					end = true;
					break;
				}
				String text = object2.getString("text");
				JSONObject userObject = (JSONObject) object2.get("user");
				if (userObject == null) {
					continue;
				}
				long sinaUserId = userObject.getLong("id");
				int returnCode = sourceService.addSource(weiboId, sinaUserId, text, SourceType.SINAWEIBO.getValue());
				if (returnCode < 0) {
					logger.error("addSinaWeiBo error where sinaWeiBoId=" + weiboId);
					break;
				}
			}
			// 如果没有结束，继续
			if (!end) {
				page++;
				this.getAtMeSinaWeiBo(Long.valueOf(SystemConst.getValue("ADMINUSERSINAID")), accessToken, biggestId, 0, 0, 0, 50, page);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#sendSinaWeiBoCallBack(long)
	 */
	public boolean sendSinaWeiBoCallBack(long sinaWeiBo, String comment) {
		weibo.setToken(accessToken);
		Comments comments = new Comments();
		try {
			comments.createComment(comment, String.valueOf(sinaWeiBo), 0);
		} catch (WeiboException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#checkSinaWeiBoUser(long,
	 * java.lang.String)
	 */
	public boolean checkSinaWeiBoUser(long accessUserId, String accessToken) {
		Weibo weibo = new Weibo();
		weibo.setToken(accessToken);
		Account weiboAccount = new Account();
		try {
			weibo4j.org.json.JSONObject jsonObject = weiboAccount.getUid();
			if (jsonObject != null && jsonObject.get("uid") != null) {
				return true;
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#getWeiBoList()
	 */
	public List<WeiBo> getWeiBoList() {
		return weiBoMapper.getWeiBoList(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.SinaWeiBoService#addWeiBo(com.ridding.meta.WeiBo)
	 */
	@Override
	public boolean addWeiBo(WeiBo weiBo) {
		return weiBoMapper.addWeiBo(weiBo) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#sendWeiBoQuartz()
	 */
	public void sendWeiBoQuartz() {
		logger.info("sendWeiBoQuartz begin!");
		SourceAccount sourceAccount = profileService.getSourceAccountByAccessUserId(Long.valueOf(SystemConst.getValue("ADMINUSERSINAID")),
				SourceType.SINAWEIBO.getValue());
		if(sourceAccount==null){
			return;
		}
		Weibo weibo = new Weibo();
		weibo.setToken(sourceAccount.getAccessToken());
		long time = new Date().getTime();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", WeiBo.notDeal);
		map.put("limit", 1);
		map.put("sendTime", time);
		WeiBo weiBo = weiBoMapper.getWeiBoByStatusTime(map);
		if (weiBo == null) {
			return;
		}

		StringBuilder sb = new StringBuilder("https://api.weibo.com/2/statuses/upload_url_text.json");
		sb.append("?access_token=" + sourceAccount.getAccessToken());
		sb.append("&source=" + SystemConst.getValue("WEBAPPKEY"));
		int result = -1;
		String response = null;
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod();
			post.setURI(new URI(sb.toString(), true));
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			post.setParameter("status", weiBo.getText());
			post.setParameter("access_token", sourceAccount.getAccessToken());
			post.setParameter("url", SystemConst.getValue("IMAGEHOST") + weiBo.getPhotoUrl());
			result = httpclient.executeMethod(post);
			response = post.getResponseBodyAsString();
			logger.info("sb=" + sb.toString() + " status=" + weiBo.getText() + " access_Token=" + sourceAccount.getAccessToken() + " url="
					+ SystemConst.getValue("IMAGEHOST") + weiBo.getPhotoUrl());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (result == 200 && !StringUtils.isEmpty(response)) {
			JSONObject jsonObject = JSONObject.fromObject(response);
			weiBo.setStatus(WeiBo.Dealed);
			map.put("status", WeiBo.Dealed);
			map.put("id", weiBo.getId());
			map.put("weiboId", jsonObject.get("id"));
			weiBoMapper.updateWeiBoStatus(map);
		} else {
			logger.error("getAtMeSinaWeiBo return code error and code=" + result + " and result=" + response);
		}
		logger.info("sendWeiBoQuartz end!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.SinaWeiBoService#genMapFromMapWeiBoQuartz()
	 */
	public void genMapFromMapWeiBoQuartz() {
		logger.info("genMapFromMapWeiBoQuartz begin!");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("weiboType", WeiBoType.RIDDING.getValue());
		map.put("sourceType", SourceType.SINAWEIBO.getValue());
		// 一个月内的微博
		long time = TimeUtil.getDayBefore(new Date().getTime(), 30);
		map.put("sendTime", time);
		map.put("status", WeiBo.Dealed);
		List<WeiBo> weiboList = weiBoMapper.getWeiBoListByWeiBoType(map);
		if (ListUtils.isEmptyList(weiboList)) {
			return;
		}
		for (WeiBo weiBo : weiboList) {
			String response = this.getRepostWeiBo(weiBo.getWeiboId());
			if (response == null) {
				return;
			}
			JSONObject jsonObject = JSONObject.fromObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("reposts");
			if (jsonArray == null) {
				continue;
			}
			for (int i = 0; i < jsonArray.size(); i++) {
				try {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					long weiboId = transactionService.insertRepostMap(weiBo, jsonObject2);
					if (weiboId > 0) {
						sourceService.sendObjectCallBack(weiboId, SourceType.SINAWEIBO.getValue(), "您转发的骑行活动已经生成，赶快去看看吧!!! "
								+ SystemConst.getValue("HOST"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		logger.info("genMapFromMapWeiBoQuartz end deal count=" + weiboList.size());
	}

	/**
	 * 得到通过weiboId得到转发的微博的列表
	 * 
	 * @param weiboId
	 * @return
	 */
	public String getRepostWeiBo(long weiboId) {
		if (weiboId <= 0) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("weiboId", weiboId);
		List<RepostMap> repostMaps = repostMapWeiBoMapper.getMaxRepostMapByWeiBoId(map);
		long sinceId = 0;
		if (!ListUtils.isEmptyList(repostMaps)) {
			sinceId = repostMaps.get(0).getRespostWeiBoId();
		}
		SourceAccount sourceAccount = profileService.getSourceAccountByAccessUserId(Long.valueOf(SystemConst.getValue("ADMINUSERSINAID")),
				SourceType.SINAWEIBO.getValue());
		if (sourceAccount == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(SystemConst.getValue("SINAHOST") + "/statuses/repost_timeline.json");
		sb.append("?access_token=" + sourceAccount.getAccessToken());
		sb.append("&id=" + weiboId);
		sb.append("&since_id=" + ++sinceId);
		int result = -1;
		String response = null;
		try {
			HttpClient httpclient = new HttpClient();
			GetMethod get = new GetMethod();
			get.setURI(new URI(sb.toString(), true, "utf-8"));
			result = httpclient.executeMethod(get);
			response = get.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (result == 200 && !StringUtils.isEmpty(response)) {
			return response;
		} else {
			logger.error("getAtMeSinaWeiBo return code error and code=" + result + " and result=" + response);
		}
		return null;
	}

}

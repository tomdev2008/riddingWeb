package com.ridding.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.dao.CityDao;
import com.ridding.mapper.IMapMapper;
import com.ridding.mapper.MapFixMapper;
import com.ridding.mapper.RiddingMapper;
import com.ridding.meta.City;
import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;
import com.ridding.meta.Ridding;
import com.ridding.service.MapService;
import com.ridding.service.SourceService;
import com.ridding.service.transaction.TransactionService;
import com.ridding.util.ListUtils;
import com.ridding.util.StringUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-29 01:58:05 Class Description
 */
@Service("mapService")
public class MapServiceImpl implements MapService {
	private static final Logger logger = Logger.getLogger(MapServiceImpl.class);
	@Resource
	private IMapMapper iMapMapper;

	@Resource
	private RiddingMapper riddingMapper;

	@Resource
	private TransactionService transactionService;

	@Resource
	private SourceService sourceService;

	@Resource
	private CityDao cityDao;

	@Resource
	private MapFixMapper mapFixMapper;

	/**
	 * 将google地图地址转成经纬度信息
	 */
	public void getMapToLocationQuartz() {
		int limit = 100;
		int offset = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("offset", offset);
		map.put("status", IMap.NotUsing);
		List<IMap> iMapList = iMapMapper.getRiddingMapListByStatus(map);
		if (!ListUtils.isEmptyList(iMapList)) {
			offset = offset + limit;
			for (IMap iMap : iMapList) {
				String mapTaps = this.extraGoogleMapUrl4DB(iMap.getMapUrl());
				if (mapTaps == null) {
					iMapMapper.deleteRiddingMap(iMap.getId());
					continue;
				}
				iMap.setMapTaps(mapTaps);

				if (this.updateMapCreateRidding(iMap)) {
					// 对外部资源做回复
					sourceService.sendObjectCallBack(iMap.getObjectId(), iMap.getObjectType(), "你提交的地图已经通过啦~速度打开手机看吧^_^");
				}
			}
		}
	}

	/**
	 * 更新地图创建骑行
	 */
	public boolean updateMapCreateRidding(IMap iMap) {
		Ridding ridding = new Ridding();
		ridding.setName("新浪骑行");
		ridding.setLeaderUserId(iMap.getUserId());
		iMap.setStatus(IMap.Using);
		return transactionService.insertANewRidding(iMap, ridding);
	}

	/**
	 * 提取出google地图
	 */
	@Override
	public String extraGoogleMapUrl(String mapUrl) {
		logger.info("extraGoogleMapUrl begin mapUrl=" + mapUrl);
		List<String> matchList = StringUtil.pattern("http://.*.google.*/maps\\?saddr=.*&daddr=.*", mapUrl);
		// 判断是否是长地址
		if (matchList == null || matchList.size() == 0) {
			logger.info("extraGoogleMapUrl shortUrl begin mapUrl=" + mapUrl);
			matchList = StringUtil.pattern("http://g.co/maps/.*", mapUrl);
			// 如果不是短地址
			if (matchList == null || matchList.size() == 0) {
				return null;
			}
		}
		// 如果是长地址
		return mapUrl;
	}

	/**
	 * 从数据库中取出google地址，并转换
	 */
	private String extraGoogleMapUrl4DB(String mapUrl) {
		if (mapUrl == null) {
			return null;
		}
		mapUrl = mapUrl.trim();
		List<String> matchList = StringUtil.pattern("(http|https)://.*.google.*/maps\\?saddr=.*&daddr=.*", mapUrl);
		// 如果数据库里的是长地址
		if (matchList == null || matchList.size() == 0) {
			matchList = StringUtil.pattern("(http|https)://g.co/maps/.*", mapUrl);
			// 如果是短地址 ，转换成短地址
			if (matchList == null || matchList.size() == 0) {
				return this.extraGoogleShortMapUrl(mapUrl);
			}
		}
		// 解析长地址
		return this.extraGoogleLongMapUrl(mapUrl);
	}

	/**
	 * 解析长地址
	 * 
	 * @param mapUrl
	 * @return
	 */
	private String extraGoogleLongMapUrl(String mapUrl) {
		JSONArray jsonArray = new JSONArray();
		String location = StringUtil.getSubString(mapUrl, "?saddr=", "&daddr=");
		String location1 = StringUtil.getSubString(mapUrl, "&daddr=", "+to:");
		if (location == null) {
			return null;
		} else if (location1 == null) {
			jsonArray.add(location);
			String location2 = StringUtil.getSubString(mapUrl, "&daddr=", "&hl=");
			if (location2 != null) {
				jsonArray.add(location2);
			}
		} else {
			jsonArray.add(location);
			jsonArray.add(location1);
			int begin = 0, beginIndex = 0, endIndex = 0;
			boolean hasTo = true;
			while (hasTo) {
				beginIndex = mapUrl.indexOf("+to:", begin);
				endIndex = mapUrl.indexOf("+to:", beginIndex + "+to:".length());
				if (beginIndex > 0 && endIndex > 0 && endIndex > beginIndex) {
					location = mapUrl.substring(beginIndex + "+to:".length(), endIndex);
					jsonArray.add(location);
					begin = endIndex;
				} else {
					hasTo = false;
				}
			}
			beginIndex = mapUrl.indexOf("+to:", begin);
			endIndex = mapUrl.indexOf("&hl", beginIndex + "+to:".length());
			if (beginIndex > 0 && endIndex > 0 && endIndex > beginIndex) {
				location = mapUrl.substring(beginIndex + "+to:".length(), endIndex);
				jsonArray.add(location);
			} else {
				return null;
			}
		}
		return jsonArray.toString();
	}

	/**
	 * 解析google短地址
	 * 
	 * @param mapUrl
	 * @param jsonArray
	 */
	private String extraGoogleShortMapUrl(String mapUrl) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(mapUrl);
			conn = (HttpURLConnection) url.openConnection();
			System.out.println("返回码: " + conn.getResponseCode());
			String longMapUrl = conn.getURL().toString();
			System.out.println(longMapUrl);
			if (!StringUtils.isEmpty(longMapUrl) && !longMapUrl.contains("(http|https)://g.co/maps")) {
				return this.extraGoogleLongMapUrl(longMapUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.MapService#deleteRiddingMap(long)
	 */
	@Override
	public int deleteRiddingMap(long id) {
		return iMapMapper.deleteRiddingMap(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.MapService#updateRiddingMap(com.ridding.meta.IMap)
	 */
	@Override
	public boolean updateRiddingMap(IMap iMap) {
		if (iMap == null) {
			return false;
		}
		return iMapMapper.updateRiddingMap(iMap) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.MapService#getMapByRiddingId(long)
	 */
	@Override
	public IMap getMapByRiddingId(long riddingId) {
		Ridding ridding = riddingMapper.getRidding(riddingId);
		if (ridding == null || ridding.getMapId() == 0) {
			return null;
		}
		return iMapMapper.getRiddingMap(ridding.getMapId());
	}

	/**
	 * ͨ通过id得到地图
	 */
	@Override
	public IMap getMapById(long id, int status) {
		IMap iMap = iMapMapper.getRiddingMap(id);
		if (iMap.getStatus() != status || iMap == null) {
			return null;
		}
		return iMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.MapService#getImapByObject(java.util.List, int,
	 * int, int)
	 */
	@Override
	public List<IMap> getImapByObject(List<Long> objectIdList, int objectType, int limit, int offset) {
		if (ListUtils.isEmptyList(objectIdList)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("offset", offset);
		map.put("objectIdList", objectIdList);
		map.put("objectType", objectType);
		return iMapMapper.getImapByObject(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.MapService#getRecomMaps(java.lang.String)
	 */
	@Override
	public List<IMap> getRecomMaps(String cityName) {
		long cityId = this.getCityIdByName(cityName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityId", cityId);
		map.put("offset", 0);
		map.put("limit", 10);
		// userId写死
		map.put("userId", 1);
		List<IMap> iMaps = iMapMapper.getImapsByUserIdCityId(map);
		if (!ListUtils.isEmptyList(iMaps)) {
			Random random = new Random();
			int key = random.nextInt(iMaps.size()) + 1;
			List<IMap> maps = new ArrayList<IMap>();
			maps.add(iMaps.get(key));
			return maps;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.MapService#getCityIdByName(java.lang.String)
	 */
	@Override
	public long getCityIdByName(String name) {
		List<City> cities = cityDao.getCitybyName(name);
		if (ListUtils.isEmptyList(cities)) {
			return -1L;
		}
		if (cities.size() > 1) {
			for (City city : cities) {
				if (city.getName().equals(name) || city.getName().equals(name + "市")) {
					return city.getId();
				}
			}
		}
		return cities.get(0).getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.MapService#getMapFix(float, float)
	 */
	@Override
	public MapFix getMapFix(double latitude, double longtitude) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("latitude", MapFix.getLatPrefix(latitude));
		hashMap.put("longtitude", MapFix.getLngPrefix(longtitude));
		logger.info(latitude + "   " + MapFix.getLatPrefix(latitude));
		logger.info(longtitude + "   " + MapFix.getLngPrefix(longtitude));
		MapFix mapFix = mapFixMapper.getMapFixByLatLng(hashMap);
		if (mapFix != null) {
			mapFix.setLatitude(latitude);
			mapFix.setLongtitude(longtitude);
			mapFix.setRealLat(latitude);
			mapFix.setRealLng(longtitude);
		}
		return mapFix;
	}

	@Override
	public List<IMap> getAllMaps() {
		List<IMap> imapList = iMapMapper.getAll();
		return imapList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.MapService#updateImapAvatorPicUrl(java.lang.String,
	 * long)
	 */
	@Override
	public boolean updateImapAvatorPicUrl(String url, long mapId) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("avatorPicUrl", url);
		hashMap.put("id", mapId);
		return iMapMapper.updateImapAvatorPicUrl(hashMap) > 0;
	}
}

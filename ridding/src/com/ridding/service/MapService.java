package com.ridding.service;

import java.util.List;

import com.ridding.meta.IMap;
import com.ridding.meta.MapFix;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-29 01:57:56 Class Description
 */
public interface MapService {

	/**
	 * 抽取google地图的地址
	 * 
	 * @param mapUrl
	 * @return
	 */
	public String extraGoogleMapUrl(String mapUrl);

	/**
	 * 删除骑行地图
	 * 
	 * @param id
	 * @return
	 */
	public int deleteRiddingMap(long id);

	/**
	 * 更新骑行地图
	 * 
	 * @param iMap
	 * @return
	 */
	public boolean updateRiddingMap(IMap iMap);

	/**
	 * 得到骑行地图
	 * 
	 * @param riddingId
	 * @return
	 */
	public IMap getMapByRiddingId(long riddingId);

	/**
	 * 得到骑行地图
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public IMap getMapById(long id, int status);

	/**
	 * 通过objectid得到对象的地图列表
	 * 
	 * @param objectIdList
	 * @param objectType
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<IMap> getImapByObject(List<Long> objectIdList, int objectType, int limit, int offset);

	/**
	 * 得到推荐的地图列表,目前只返回一个
	 * 
	 * @param cityName
	 * @return
	 */
	public List<IMap> getRecomMaps(String cityName);

	/**
	 * 得到城市的id ,通过名称
	 * 
	 * @param name
	 * @return
	 */
	public long getCityIdByName(String name);

	/**
	 * 得到mapFix
	 * 
	 * @param latitude
	 * @param longtitude
	 * @return
	 */
	public MapFix getMapFix(double latitude, double longtitude);
	/**
	 * 获取所有地图
	 * 
	 * @return
	 */
	public List<IMap> getAllMaps();
	
	/**
	 * 根据相应ID更新地图URL
	 * 
	 * @param url
	 * @param mapId
	 * @return
	 */
	public boolean updateImapAvatorPicUrl(String url,long mapId);
}

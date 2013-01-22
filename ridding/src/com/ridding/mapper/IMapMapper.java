package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.IMap;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 12:50:25 Class Description
 */
public interface IMapMapper {
	/**
	 * 得到骑行地图
	 * 
	 * @param riddingId
	 * @return
	 */
	public IMap getRiddingMap(long id);

	/**
	 * 添加一个骑行地图
	 * 
	 * @param iMap
	 * @return
	 */
	public int addRiddingMap(IMap iMap);

	/**
	 *通过状态得到地图,status，limit,offset
	 * 
	 * @return
	 */
	public List<IMap> getRiddingMapListByStatus(Map<String, Object> hashMap);

	/**
	 * 更新骑行地图
	 * 
	 * @param riddingId
	 * @param status
	 * @param mapTaps
	 * @return
	 */
	public int updateRiddingMap(IMap iMap);

	/**
	 *删除骑行地图
	 * 
	 * @param riddingId
	 * @return
	 */
	public int deleteRiddingMap(long id);

	/**
	 * 得到骑行列表通过id列表
	 * 
	 * @param iMapList
	 * @return
	 */
	public List<IMap> getIMaplist(List<Long> iMapList);

	/**
	 * 通过objectid，objecttype得到list
	 * 
	 * @param hashMap
	 * @return
	 */
	public List<IMap> getImapByObject(Map<String, Object> hashMap);

	/**
	 * 新用户登陆是用,不建索引
	 * 
	 * @return
	 */
	public List<IMap> getImapsByUserIdCityId(Map<String, Object> hashMap);
	
	/**
	 * 获取所有地图
	 * 
	 * @return
	 */
	public List<IMap> getAll();
	
	/**
	 * 更新相应ID的地图URL
	 * 
	 * @param url
	 * @param mapId
	 * @return
	 */
	public int updateImapAvatorPicUrl(String url,long mapId);
}

package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.RepostMap;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-11-10 上午12:02:45 Class Description
 */
public interface RepostMapWeiBoMapper {
	/**
	 * 添加记录
	 * 
	 * @param repostMap
	 * @return
	 */
	public int addRepostMap(RepostMap repostMap);

	/**
	 * 得到某微博当前被转发的微博的最新值
	 * 
	 * @param map
	 * @return
	 */
	public List<RepostMap> getMaxRepostMapByWeiBoId(Map<String, Object> map);

	/**
	 * 通过微博id和转发的用户id得到
	 * 
	 * @param map
	 * @return
	 */

	public RepostMap getRepostMapByWeiBoIdReposterId(Map<String, Object> map);
}

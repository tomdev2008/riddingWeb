package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.WeiBo;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-10-22 下午11:59:49 Class Description
 */
public interface WeiBoMapper {
	/**
	 * 添加微博
	 * 
	 * @param weiBo
	 * @return
	 */
	public int addWeiBo(WeiBo weiBo);

	/**
	 * 得到微博
	 * 
	 * @param map
	 * @return
	 */
	public WeiBo getWeiBoByStatusTime(Map<String, Object> map);

	/**
	 * 更新微博
	 * 
	 * @param map
	 * @return
	 */
	public int updateWeiBoStatus(Map<String, Object> map);

	/**
	 * 得到微博列表
	 * 
	 * @param map
	 * @return
	 */
	public List<WeiBo> getWeiBoList(Map<String, Object> map);

	/**
	 * 通过微博类型得到微博
	 * 
	 * @param map
	 * @return
	 */
	public List<WeiBo> getWeiBoListByWeiBoType(Map<String, Object> map);
}

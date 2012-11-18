package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.Source;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-30 03:58:08 Class Description
 */
public interface SourceMapper {
	/**
	 * 添加资源
	 * 
	 * @param sinaWeiBo
	 * @return
	 */
	public int addSource(Source source);

	/**
	 * 得到资源列表
	 * 
	 * @param status
	 * @return
	 */
	public List<Source> getSourceListNoType(Map<String, Object> hashMap);

	/**
	 * 更新新浪微博状态
	 * 
	 * @param Id
	 * @param status
	 * @return
	 */
	public int updateSourceStatus(Source source);

	/**
	 * 得到新浪微博最大id
	 * 
	 * @return
	 */
	public Long getBigestId(int sourceType);

	/**
	 * 通过id获取资源
	 * 
	 * @param id
	 * @return
	 */
	public Source getSourceBySourceId(Map<String, Object> hashMap);

	/**
	 *通过状态和类型得到资源数量,status,sourceType
	 * 
	 * @param status
	 * @return
	 */
	public int getSourceCountByStatus(Map<String, Object> hashMap);

}

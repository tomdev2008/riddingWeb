package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.Public;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-2 下午11:39:00 Class Description
 */
public interface PublicMapper {

	/**
	 * 添加广告信息
	 * 
	 * @return
	 */
	public int addPublic(Public p);

	/**
	 * 根据类型得到广告
	 * 
	 * @param type
	 * @return
	 */
	public List<Public> getPublicListsByType(Map<String, Object> map);

	/**
	 * 通过Id得到广告
	 * 
	 * @param publicId
	 * @return
	 */
	public Public getPublicById(long publicId);

	/**
	 * 更新广告内容
	 * 
	 * @param map
	 * @return
	 */
	public boolean updatePublic(Map<String, Object> map);

	/**
	 * 删除广告
	 * 
	 * @param publicId
	 * @return
	 */
	public boolean deletePublicByPublicId(long publicId);
}

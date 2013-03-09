package com.ridding.service;

import java.util.List;

import com.ridding.meta.Public;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-2 下午11:45:38 Class Description
 */

public interface PublicService {
	/**
	 * 添加广告
	 * 
	 * @param type
	 * @param jsonStr
	 * @param weight
	 * @return
	 */
	public boolean addPublic(int type, String jsonStr, int weight);

	/**
	 * 通过类型得到广告类型
	 * 
	 * @param type
	 * @param limit
	 * @param weight
	 * @return
	 */
	public List<Public> getPublicListByType(int type, int limit, int weight, boolean isLarger);

	/**
	 * 更新广告
	 * 
	 * @param riddingId
	 * @param picUrl
	 * @return
	 */
	public boolean updatePublicFirstPicUrl(long id, String picUrl);

	/**
	 * 添加
	 * 
	 * @param aPublic
	 * @return
	 */
	public boolean addPublic(Public aPublic);

	/**
	 * 通过Id获得广告
	 * 
	 * @param publicId
	 * @return
	 */
	public Public getPublicById(long publicId);

	/**
	 * 更新广告内容
	 * 
	 * @param jsonStr
	 * @return
	 */
	public boolean updatePublic(long publicId, String jsonStr);

	/**
	 * 删除广告
	 * 
	 * @param publicId
	 * @return
	 */
	public boolean deletePublicByPublicId(long publicId);
}

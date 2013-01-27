package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.RiddingPicture;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-17 下午09:36:26 Class Description 骑行图片
 */
public interface RiddingPictureMapper {

	/**
	 * 添加图片
	 * 
	 * @param riddingPicture
	 * @return
	 */
	public int addRiddingPicture(RiddingPicture riddingPicture);

	/**
	 * 删除骑行图片
	 * 
	 * @param riddingPictureId
	 * @return
	 */
	public int deleteRiddingPicture(long id);

	/**
	 * 得到骑行图片
	 * 
	 * @param map
	 * @return
	 */
	public List<RiddingPicture> getRiddingPicturesByRiddingId(Map<String, Object> map);
	
	/**
	 * 通过riddingId删除骑行图片
	 * 
	 */
	public int deleteRiddingPicByRiddingId(long riddingId);
}

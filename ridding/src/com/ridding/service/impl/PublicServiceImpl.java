package com.ridding.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.PublicMapper;
import com.ridding.meta.Public;
import com.ridding.meta.Public.PublicType;
import com.ridding.service.PublicService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-2 下午11:45:46 Class Description
 */
@Service("publicService")
public class PublicServiceImpl implements PublicService {
	@Resource
	private PublicMapper publicMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#addPublic(int, java.lang.String,
	 * int)
	 */
	public boolean addPublic(int type, String jsonStr, int weight) {
		Public p = new Public();
		p.setType(type);
		p.setJson(jsonStr);
		p.setWeight(weight);
		long nowTime = new Date().getTime();
		p.setCreateTime(nowTime);
		p.setLastUpdateTime(nowTime);
		return publicMapper.addPublic(p) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#addPublic(com.ridding.meta.Public)
	 */
	public boolean addPublic(Public aPublic) {
		return publicMapper.addPublic(aPublic) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#getPublicListByType(int, int, int)
	 */
	public List<Public> getPublicListByType(int type, int limit, int weight, boolean isLarger) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("limit", limit);
		map.put("weight", weight);
		map.put("isLarger", isLarger ? 1 : 0);
		return publicMapper.getPublicListsByType(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#updatePublicFirstPicUrl(long,
	 * java.lang.String)
	 */
	public boolean updatePublicFirstPicUrl(long id, String picUrl) {
		Public public1 = publicMapper.getPublicById(id);
		if (public1 == null) {
			return false;
		}
		if (PublicType.genPublicType(public1.getType()) != PublicType.PublicRecom) {
			return false;
		}
		public1.setFirstPicUrl(picUrl);
		public1.genJson();
		return publicMapper.updateJsonById(id, public1.getJson()) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#getPublicById(long)
	 */
	public Public getPublicById(long publicId) {
		return publicMapper.getPublicById(publicId);
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#updatePublic(long,
	 * java.lang.String)
	 */
	public boolean updatePublic(long publicId, String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("publicId", publicId);
		map.put("jsonStr", jsonStr);
		return publicMapper.updatePublic(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.PublicService#deletePublicByPublicId(long)
	 */
	public boolean deletePublicByPublicId(long publicId) {
		return publicMapper.deletePublicByPublicId(publicId);
	}
}

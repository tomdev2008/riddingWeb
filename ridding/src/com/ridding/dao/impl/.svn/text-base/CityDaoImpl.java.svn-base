package com.ridding.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ridding.dao.CityDao;
import com.ridding.mapper.CityMapper;
import com.ridding.meta.City;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-7-16 下午10:19:22 Class Description
 */
@Repository("cityDao")
public class CityDaoImpl implements CityDao {

	@Resource
	private CityMapper cityMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.dao.CityDao#getCitybyName(java.lang.String)
	 */
	@Override
	public List<City> getCitybyName(String name) {
		return cityMapper.getCitybyName("%" + name + "%");
	}

}

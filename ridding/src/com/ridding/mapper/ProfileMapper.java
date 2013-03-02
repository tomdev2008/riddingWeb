package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ridding.meta.Profile;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 12:50:33 Class Description
 */
public interface ProfileMapper {
	/**
	 * 添加用户信息
	 * 
	 * @param profile
	 * @return
	 */
	public int addProfile(Profile profile);

	/**
	 * 更新用户信息
	 * 
	 * @param profile
	 * @return
	 */
	public int updateProfile(Profile profile);

	/**
	 * 更新灰色图
	 * 
	 * @param userId
	 * @param graySavatorUrl
	 * @return
	 */
	public int updategraysAvator(Profile profile);

	/**
	 * 得到用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Profile getProfile(long userId);

	/**
	 * 增加用户总的骑行距离
	 * 
	 * @param hashMap
	 * @return
	 */
	public int incUserTotalDistance(Map<String, Object> hashMap);

	/**
	 * 得到用户信息列表
	 * 
	 * @param userids
	 * @return
	 */
	public List<Profile> getProfileList(@Param("userids") List<Long> userids);

	/**
	 * 更新手机背景url
	 * 
	 * @param profile
	 * @return
	 */
	public int updateBackgroundUrl(Profile profile);
	
	public List<Profile> getAllProfile();
}

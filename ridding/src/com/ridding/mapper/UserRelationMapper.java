package com.ridding.mapper;

import java.util.List;
import java.util.Map;

import com.ridding.meta.UserRelation;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2013-1-3 下午11:37:42 Class Description
 */
public interface UserRelationMapper {
	/**
	 * 添加用户关系
	 * 
	 * @param userRelation
	 * @return
	 */
	public int addUserRelation(UserRelation userRelation);

	/**
	 * 得到某个用户的关系
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRelation> getUserRelations(long userId);

	/**
	 * 得到用户关系
	 * 
	 * @param map
	 * @return
	 */
	public UserRelation getUserRelation(Map<String, Object> map);

	/**
	 * 更新用户关系
	 * 
	 * @param userRelation
	 * @return
	 */
	public int updateUserRelation(UserRelation userRelation);
}

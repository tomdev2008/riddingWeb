package com.ridding.service;

import java.util.List;

import com.ridding.meta.Source;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-5-12 下午09:06:10 Class Description
 */
public interface SourceService {
	/**
	 * 添加资源
	 * 
	 * @param sourceId
	 * @param accessUserId
	 * @param text
	 * @param sourceType
	 * @return
	 */
	public int addSource(long sourceId, long accessUserId, String text, int sourceType);

	/**
	 * 更新资源到某个状态
	 * 
	 * @param sourceId
	 * @param status
	 * @param sourceType
	 * @return
	 */
	public boolean updateSource(long sourceId, int status, int sourceType);

	/**
	 * 通过资源id得到资源
	 * 
	 * @param sourceId
	 * @param sourceType
	 * @return
	 */
	public Source getSourceBySourceId(long sourceId, int sourceType);

	/**
	 * 得到包含账户信息的资源列表
	 * 
	 * @param status
	 * @param limit
	 * @param offset
	 * @param sourceType
	 * @return
	 */
	public List<Source> getSourceListWithAccount(int status, int limit, int offset, int sourceType);

	/**
	 * 通过状态得到某资源数量
	 * 
	 * @param status
	 * @param sourceType
	 * @return
	 */
	public int getSourceCountByStatus(int status, int sourceType);

	/**
	 * 得到已经处理的但是不在地图中的资源类型
	 * 
	 * @param status
	 * @param limit
	 * @param offset
	 * @param sourceType
	 * @return
	 */
	public List<Source> getDealedNotInMap(int status, int limit, int offset, int sourceType);

	/**
	 * 得到最大id
	 * 
	 * @return
	 */
	public long getBigestId(int sourceType);

	/**
	 * 添加地图成功后发送消息
	 * 
	 * @param objectId
	 * @param objectType
	 */
	public void sendObjectCallBack(long objectId, int objectType, String comment);

	/**
	 * 检测资源是否正确
	 * 
	 * @param accessToken
	 * @param accessUserId
	 * @param sourceType
	 * @return
	 */
	public boolean checkSourceUser(String accessToken, long accessUserId, int sourceType);
}

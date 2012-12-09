package com.ridding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.mapper.ProfileMapper;
import com.ridding.mapper.RiddingCommentMapper;
import com.ridding.meta.Profile;
import com.ridding.meta.RiddingComment;
import com.ridding.service.RiddingCommentService;
import com.ridding.util.HashMapMaker;
import com.ridding.util.ListUtils;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-8 下午11:41:37 Class Description
 */
@Service("riddingCommentService")
public class RiddingCommentServiceImpl implements RiddingCommentService {
	@Resource
	private RiddingCommentMapper riddingCommentMapper;
	@Resource
	private ProfileMapper profileMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.RiddingCommentService#addRiddingComment(com.ridding
	 * .meta.RiddingComment)
	 */
	@Override
	public boolean addRiddingComment(RiddingComment riddingComment) {
		if (riddingComment == null) {
			return false;
		}
		return riddingCommentMapper.addRiddingComment(riddingComment) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.RiddingCommentService#getRiddingComments(long,
	 * int, java.lang.Boolean)
	 */
	public List<RiddingComment> getRiddingComments(long createTime, int limit, boolean isLarger) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createTime", createTime);
		map.put("limit", limit);
		map.put("isLarger", isLarger ? 1 : 0);
		List<RiddingComment> riddingCommentList = riddingCommentMapper.getRiddingCommentList(map);
		this.insertProfileInfo(riddingCommentList);
		return riddingCommentList;
	}

	private void insertProfileInfo(List<RiddingComment> riddingCommentList) {
		if (ListUtils.isEmptyList(riddingCommentList)) {
			return;
		}
		List<Long> userIds = new ArrayList<Long>(riddingCommentList.size());
		List<Long> toUserIds = new ArrayList<Long>(riddingCommentList.size());
		for (RiddingComment riddingComment : riddingCommentList) {
			userIds.add(riddingComment.getUserId());
			toUserIds.add(riddingComment.getToUserId());
		}
		List<Profile> userProfiles = profileMapper.getProfileList(userIds);
		Map<Long, Profile> userProfileMap = HashMapMaker.listToMap(userProfiles, "getUserId", Profile.class);
		List<Profile> toUserProfiles = profileMapper.getProfileList(toUserIds);
		Map<Long, Profile> toUserProfileMap = HashMapMaker.listToMap(toUserProfiles, "getUserId", Profile.class);
		for (RiddingComment riddingComment : riddingCommentList) {
			Profile userProfile = userProfileMap.get(riddingComment.getUserId());
			if (userProfile != null) {
				riddingComment.setUserProfile(userProfile);
			}
			Profile toUserProfile = toUserProfileMap.get(riddingComment.getToUserId());
			if (toUserProfile != null) {
				riddingComment.setToUserProfile(toUserProfile);
			}
		}

	}
}

package com.ridding.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.meta.Club;
import com.ridding.meta.ClubMember;
import com.ridding.meta.ClubApply;
import com.ridding.meta.ClubMemberApply;
import com.ridding.meta.ClubMember.ClubMemberRoleType;
import com.ridding.service.ClubService;

import com.ridding.mapper.ClubMapper;
import com.ridding.mapper.ClubApplyMapper;
import com.ridding.mapper.ClubMemberApplyMapper;
import com.ridding.mapper.ClubMemberMapper;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:20:14 Class Description
 */
@Service("clubService")
public class ClubServiceImpl implements ClubService {
	@Resource
	private ClubMapper clubMapper;

	@Resource
	private ClubMemberMapper clubMemberMapper;

	@Resource
	private ClubApplyMapper clubApplyMapper;

	@Resource
	private ClubMemberApplyMapper clubMemberApplyMapper;

	private static final Logger logger = Logger.getLogger(ClubServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#applyForCreateClub(long,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean applyForCreateClub(long userId, String clubName, String clubLocation, String clubDescription) {
		ClubApply clubApply = new ClubApply();
		clubApply.setUserId(userId);
		clubApply.setClubName(clubName);
		clubApply.setClubLocation(clubLocation);
		clubApply.setClubDescription(clubDescription);
		clubApply.setCreateTime(new Date().getTime());
		clubApply.setStatus(Club.ApplyStatus.Waiting.getValue());

		ClubApply returnClubApply = clubApplyMapper.getcClubApplyByClubName(clubName);
		if (returnClubApply != null) {
			if (returnClubApply.getStatus() == Club.ApplyStatus.Agree.getValue()
					|| returnClubApply.getStatus() == Club.ApplyStatus.Waiting.getValue()) {
				return false;
			}
			if (clubApplyMapper.updateClubApply(clubApply) > 0) {
				return true;
			}
			return false;
		}

		if (clubApplyMapper.addClubApply(clubApply) > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#applyForJoinClub(long, long)
	 */
	@Override
	public boolean applyForJoinClub(long userId, long clubId) {
		ClubMemberApply clubMemberApply = new ClubMemberApply();
		clubMemberApply.setUserId(userId);
		clubMemberApply.setClubId(clubId);
		clubMemberApply.setCreateTime(new Date().getTime());
		clubMemberApply.setStatus(Club.ApplyStatus.Waiting.getValue());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("clubId", clubId);
		ClubMemberApply returnclubClubMemberApply = clubMemberApplyMapper.getClubMemberApplyByUserIdAndClubId(map);

		if (returnclubClubMemberApply != null) {
			if (returnclubClubMemberApply.getStatus() == Club.ApplyStatus.Agree.getValue()
					|| returnclubClubMemberApply.getStatus() == Club.ApplyStatus.Waiting.getValue()) {
				return false;
			}
			if (clubMemberApplyMapper.updateClubMemberApply(clubMemberApply) > 0) {
				return true;
			}
			return false;
		}

		if (clubMemberApplyMapper.addClubMemberApply(clubMemberApply) > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#agreeOrRefuseToCreateClub(long, int)
	 */
	@Override
	public boolean agreeOrRefuseToCreateClub(long applyId, int status) {
		ClubApply clubApply = new ClubApply();
		clubApply.setId(applyId);
		clubApply.setStatus(status);
		clubApply.setLastUpdateTime(new Date().getTime());
		if (clubApplyMapper.updateClubApply(clubApply) > 0) {
			if (status == Club.ApplyStatus.Agree.getValue()) {
				ClubApply returnClubApply = clubApplyMapper.getClubApply(applyId);
				if (returnClubApply == null) {
					return false;
				}
				if (clubMapper.getClubByName(returnClubApply.getClubName()) != null) {
					return false;
				}
				int clubLevel = 0, maxMembership = 10;
				Club club = new Club();
				club.setClubName(returnClubApply.getClubName());
				club.setClubLocation(returnClubApply.getClubLocation());
				club.setClubDescription(returnClubApply.getClubDescription());
				club.setCreateTime(new Date().getTime());
				club.setManagerId(returnClubApply.getUserId());
				club.setClubLevel(clubLevel);
				club.setClubCoverUrl("");
				club.setMaxMembership(maxMembership);
				if (clubMapper.addClub(club) > 0) {
					return true;
				} else {
					logger.error("Fail to create club with the applyId = " + applyId);
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#agreeOrRefuseToJoinClub(long, int)
	 */
	@Override
	public boolean agreeOrRefuseToJoinClub(long applyId, int status) {
		ClubMemberApply clubMemberApply = new ClubMemberApply();
		clubMemberApply.setId(applyId);
		clubMemberApply.setStatus(status);
		clubMemberApply.setLastUpdateTime(new Date().getTime());
		if (clubMemberApplyMapper.updateClubMemberApply(clubMemberApply) > 0) {
			if (status == Club.ApplyStatus.Agree.getValue()) {
				ClubMemberApply returnClubMemberApply = clubMemberApplyMapper.getClubMemberApply(applyId);
				if (returnClubMemberApply == null) {
					return false;
				}
				int memberType = 1;
				ClubMember clubMember = new ClubMember();
				clubMember.setUserId(returnClubMemberApply.getUserId());
				clubMember.setClubId(returnClubMemberApply.getClubId());
				clubMember.setCreateTime(new Date().getTime());
				clubMember.setMemberType(memberType);
				clubMember.setNickname("");

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("clubId", returnClubMemberApply.getClubId());
				map.put("userId", returnClubMemberApply.getUserId());
				ClubMember returnClubMember = clubMemberMapper.getClubMember(map);

				if (clubMapper.getClubByClubId(returnClubMemberApply.getClubId()) == null) {
					return false;
				}

				if (returnClubMember != null) {
					clubMember.setId(returnClubMember.getId());
					if (clubMemberMapper.updateClubMember(clubMember) > 0) {
						return true;
					}
					logger.error("Fail to update club member with applyId = " + applyId);
					return false;
				}

				if (clubMemberMapper.addClubMember(clubMember) > 0) {
					return true;
				} else {
					logger.error("Fail to create clubMember with the applyId = " + applyId);
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#addClubMember(long, long)
	 */
	@Override
	public boolean addClubMember(long userId, long clubId) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clubId", clubId);
		map.put("userId", userId);
		ClubMember returnClubMember = clubMemberMapper.getClubMember(map);
		if (returnClubMember != null && returnClubMember.getMemberType() != ClubMemberRoleType.Member.intValue()) {
			returnClubMember.setId(returnClubMember.getId());
			if (clubMemberMapper.updateClubMember(returnClubMember) > 0) {
				return true;
			}
			logger.error("Fail to update club member with userId = " + userId + ",clubId = " + clubId);
			return false;
		}

		ClubMember clubMember = new ClubMember();
		clubMember.setUserId(userId);
		clubMember.setClubId(clubId);
		clubMember.setMemberType(ClubMemberRoleType.Member.intValue());
		clubMember.setCreateTime(new Date().getTime());

		if (clubMemberMapper.addClubMember(clubMember) > 0) {
			return true;
		}

		logger.error("Fail to add club member with userId = " + userId + ",clubId = " + clubId);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#removeClubMember(long, long)
	 */
	@Override
	public boolean removeClubMember(long userId, long clubId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clubId", clubId);
		map.put("userId", userId);
		ClubMember returnClubMember = clubMemberMapper.getClubMember(map);
		if (returnClubMember != null) {
			int memberType = 0;
			ClubMember clubMember = new ClubMember();
			clubMember.setId(returnClubMember.getId());
			clubMember.setCreateTime(new Date().getTime());
			clubMember.setMemberType(memberType);
			if (clubMemberMapper.updateClubMember(clubMember) > 0) {
				return true;
			}
			logger.error("Fail to remove club member with userId = " + userId + ",clubId = " + clubId);
		}
		logger.error("There is no member with userId = " + userId + ",clubId = " + clubId);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.ClubService#getClubMember(long, long)
	 */
	@Override
	public ClubMember getClubMember(long userId, long clubId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clubId", clubId);
		map.put("userId", userId);
		return clubMemberMapper.getClubMember(map);
	}
}
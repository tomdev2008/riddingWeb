package com.ridding.bean.dwr;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.meta.ClubMember;
import com.ridding.meta.Profile;
import com.ridding.service.ClubService;
import com.ridding.service.ProfileService;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-19 下午09:04:50 Class Description
 */
@Service("dwrClubBean")
public class DwrClubBean {

	@Resource
	private ClubService clubService;

	@Resource
	private ProfileService profileService;

	/**
	 * 同意或拒绝创建俱乐部申请
	 * 
	 * @param applyId
	 * @param status
	 * @return
	 */
	public boolean agreeOrRefuseToCreateClub(long userId, long applyId,
			int status) {
		if (applyId < 0 || status < 0) {
			return false;
		}
		Profile profile = profileService.getProfile(userId);
		if (profile.getLevel() == 1) {
			return clubService.agreeOrRefuseToCreateClub(applyId, status);
		}
		return false;
	}

	/**
	 * 同意或拒绝成员加入俱乐部
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean agreeOrRefuseToJoinClub(long userId, long clubId,
			long applyId, int status) {
		if (userId < 0 || clubId < 0 || applyId < 0 || status < 0) {
			return false;
		}
		ClubMember clubMember = clubService.getClubMember(userId, clubId);
		if (clubMember.isManager()) {
			return clubService.agreeOrRefuseToJoinClub(applyId, status);
		}
		return false;
	}

	/**
	 * 添加或删除俱乐部成员
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean addOrRemoveClubMember(long userId, long toUserId,
			long clubId, int action) {
		if (userId < 0 || toUserId < 0 || clubId < 0) {
			return false;
		}
		ClubMember clubMember = clubService.getClubMember(userId, clubId);
		if (clubMember.isManager()) {
			int add = 1, remove = 2;
			if (action == add) {
				return clubService.addClubMember(toUserId, clubId);
			} else if (action == remove) {
				return clubService.removeClubMember(toUserId, clubId);
			}
		}
		return false;
	}
}

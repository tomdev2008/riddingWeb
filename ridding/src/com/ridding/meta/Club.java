package com.ridding.meta;

import java.io.Serializable;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:22:35 Class Description
 */
public class Club implements Serializable {

	private static final long serialVersionUID = -446109754231L;

	private long clubId;

	private String clubName;

	private long createTime;

	private String clubLocation;

	private int clubLevel;

	private int maxMembership;

	private long managerId;

	private String clubCoverUrl;

	private String clubDescription;

	public enum ApplyStatus {
		/**
		 * 等待通过
		 */
		Waiting {
			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 通过
		 */
		Agree {
			@Override
			public int getValue() {
				return 1;
			}
		},
		/**
		 * 拒绝
		 */
		Refuse {
			@Override
			public int getValue() {
				return 2;
			}
		};
		public abstract int getValue();

		public static ApplyStatus genApplyStatus(int t) {
			for (ApplyStatus status : ApplyStatus.values()) {
				if (status.getValue() == t)
					return status;
			}
			return null;
		}
	}

	public long getClubId() {
		return clubId;
	}

	public void setClubId(long clubId) {
		this.clubId = clubId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getClubLocation() {
		return clubLocation;
	}

	public void setClubLocation(String clubLocation) {
		this.clubLocation = clubLocation;
	}

	public int getClubLevel() {
		return clubLevel;
	}

	public void setClubLevel(int clubLevel) {
		this.clubLevel = clubLevel;
	}

	public int getMaxMembership() {
		return maxMembership;
	}

	public void setMaxMembership(int maxMembership) {
		this.maxMembership = maxMembership;
	}

	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}

	public String getClubCoverUrl() {
		return clubCoverUrl;
	}

	public void setClubCoverUrl(String clubCoverUrl) {
		this.clubCoverUrl = clubCoverUrl;
	}

	public String getClubDescription() {
		return clubDescription;
	}

	public void setClubDescription(String clubDescription) {
		this.clubDescription = clubDescription;
	}
}

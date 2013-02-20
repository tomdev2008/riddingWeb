package com.ridding.meta;

import java.io.Serializable;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-2-18 下午11:40:57 Class Description
 */
public class ClubMember implements Serializable {

	private static final long serialVersionUID = -446109754232L;

	private long id;

	private long clubId;

	private long userId;

	private String nickname;

	private long createTime;

	private int memberType;

	/**
	 * 成员类型
	 * 
	 * @author Administrator
	 * 
	 */
	public enum ClubMemberRoleType {
		/**
		 * 非管理员或成员(0)
		 */
		Nothing {
			@Override
			public int intValue() {
				return 0;
			}
		},
		/**
		 * 成员(1)
		 */
		Member {
			@Override
			public int intValue() {
				return 1;
			}
		},
		/**
		 * 管理员(2)
		 */
		Manager {
			@Override
			public int intValue() {
				return 2;
			}
		};
		public abstract int intValue();

		public static ClubMemberRoleType genRoleType(int t) {
			for (ClubMemberRoleType type : ClubMemberRoleType.values()) {
				if (type.intValue() == t)
					return type;
			}
			return null;
		}
	}

	public boolean isManager() {
		return this.memberType == ClubMemberRoleType.Manager.intValue();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClubId() {
		return clubId;
	}

	public void setClubId(long clubId) {
		this.clubId = clubId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
}

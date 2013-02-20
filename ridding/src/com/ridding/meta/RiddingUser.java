package com.ridding.meta;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 骑行用户12:37:41 Class Description 骑行用户meta
 */
public class RiddingUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5153364429495031858L;
	/**
	 * id
	 */
	private long id;
	/**
	 * 骑行 id
	 */
	private long riddingId;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 用户权限
	 */
	private int userRole;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 上次更新时间
	 */
	private long lastUpdateTime;
	/**
	 * 当前骑行状态,存在数据库中
	 */
	private int riddingStatus;
	/**
	 * 骑行名称
	 */
	private String selfName;
	/**
	 * 经度
	 */
	private double longtitude;
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 对应ridding中的骑行 状态
	 */
	private int status;
	/**
	 * 当前状态,骑行的状态，是否在进行中，默认是在进行中，存在缓存中
	 */
	private int userStatus = 1;
	/**
	 * 速度
	 */
	private double speed;
	/**
	 * 用户信息
	 */
	private Profile profile;
	/**
	 * 更新时间，多久之前,不存数据库
	 */
	private String timeBefore;
	/**
	 * 缓存的时间，不存数据库
	 */
	private long cacheTime;
	/**
	 * 是否显示骑行用户，不存数据库
	 */
	private boolean showTeamer;


	/**
	 * 插入用户当前的骑行状态
	 */
	public void setState() {
		long time = new Date().getTime();
		// 超过30分钟
		if (time - this.cacheTime > 1000 * 60 * 30) {
			this.userStatus = 0;
		}
		this.userStatus = 1;
	}

	/**
	 *骑行状态
	 * 
	 * @author apple
	 * 
	 */
	public enum SelfRiddingStatus {
		/**
		 * 还没开始 (0)
		 */
		NotBeginning {
			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 正在进行(10)
		 */
		Beginning {
			@Override
			public int getValue() {
				return 10;
			}
		},
		/**
		 * 已经完成 (20)
		 */
		Finished {
			@Override
			public int getValue() {
				return 20;
			}
		},
		/**
		 *还没结束(30)
		 */
		NotFinished {
			@Override
			public int getValue() {
				return 30;
			}
		};
		public abstract int getValue();

		public static SelfRiddingStatus genSelfRiddingStatus(int t) {
			for (SelfRiddingStatus status : SelfRiddingStatus.values()) {
				if (status.getValue() == t)
					return status;
			}
			return null;
		}
	}

	/**
	 * 是否是队员
	 * 
	 * @return
	 */
	public boolean isTeamer() {
		return this.userRole != RiddingUserRoleType.Nothing.intValue();
	}

	/**
	 * 是否是队长
	 * 
	 * @return
	 */
	public boolean isLeader() {
		return this.userRole == RiddingUserRoleType.Leader.intValue();
	}

	/**
	 * 是否只是队员
	 * 
	 * @return
	 */
	public boolean isJustATeamer() {
		return this.userRole == RiddingUserRoleType.User.intValue();
	}

	/**
	 * 骑行用户类型
	 * 
	 * @author apple
	 * 
	 */
	public enum RiddingUserRoleType {
		/**
		 * 非队员或队长(0)
		 */
		Nothing {
			@Override
			public int intValue() {
				return 0;
			}
		},
		/**
		 * 队员(10)
		 */
		User {
			@Override
			public int intValue() {
				return 10;
			}
		},
		/**
		 * 队长(20)
		 */
		Leader {
			@Override
			public int intValue() {
				return 20;
			}
		};
		public abstract int intValue();

		public static RiddingUserRoleType genHuodongType(int t) {
			for (RiddingUserRoleType type : RiddingUserRoleType.values()) {
				if (type.intValue() == t)
					return type;
			}
			return null;
		}
	}
	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public String getTimeBefore() {
		return timeBefore;
	}

	public void setTimeBefore(String timeBefore) {
		this.timeBefore = timeBefore;
	}

	public String getSelfName() {
		return selfName;
	}

	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public int getRiddingStatus() {
		return riddingStatus;
	}

	public void setRiddingStatus(int riddingStatus) {
		this.riddingStatus = riddingStatus;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isShowTeamer() {
		return showTeamer;
	}

	public void setShowTeamer(boolean showTeamer) {
		this.showTeamer = showTeamer;
	}
	

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

}

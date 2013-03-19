package com.ridding.meta;

import java.io.Serializable;
import java.util.List;

import com.ridding.util.TimeUtil;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 12:32:25 Class Description 骑行meta
 */
public class Ridding implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4179942070890520349L;
	/**
	 * id
	 */
	private long id;
	/**
	 * 队长用户id
	 */
	private long leaderUserId;
	/**
	 * 地图id
	 */
	private long mapId;
	/**
	 * 骑行的默认名称
	 */
	private String name = "";
	/**
	 * 骑行类型
	 */
	private int riddingType;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 骑行状态
	 */
	private int riddingStatus;
	/**
	 * 最近更新时间
	 */
	private long lastUpdateTime;
	/**
	 * 用户数量
	 */
	private int userCount;
	/**
	 * 喜欢数量
	 */
	private int likeCount;
	/**
	 * 评论数
	 */
	private int commentCount;
	/**
	 * 使用数
	 */
	private int useCount;
	/**
	 * 关注数
	 */
	private int careCount;
	/**
	 * 照片数
	 */
	private int pictureCount;
	/**
	 * 是否同步到新浪
	 */
	private int isSyncSina;
	/**
	 * 用户只在wifi状态下同步
	 */
	private int userSyncWifi;

	/**
	 * 限制，-1表示全部
	 */
	private int limit = -1;

	private boolean isLarger;
	/**
	 * 是否是推荐
	 */
	public int isRecom = 0;
	/**
	 * 是否是公开
	 */
	public int isPublic = 0;

	/**
	 * 非公开或者非推荐
	 */
	public static int notPublicOrRecom = 0;
	/**
	 * 公开或者推荐
	 */
	public static int PublicOrRecom = 1;
	/**
	 * 不同步
	 */
	public static int notSync = 0;
	/**
	 * 同步
	 */
	public static int sync = 1;
	/**
	 * 距离
	 */
	public int distance;
	/**
	 * 骑行图片列表
	 */
	private List<RiddingPicture> riddingPictureList;
	/**
	 * 队长的profile
	 */
	private Profile leaderProfile;
	/**
	 * 创建时间
	 */
	private String createTimeStr;

	private Public aPublic;

	/**
	 * 骑行状态
	 * 
	 * @author apple
	 * 
	 */
	public enum RiddingStatus {
		/**
		 * 还没开始
		 */
		NotBeginning {
			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 正在进行
		 */
		Beginning {
			@Override
			public int getValue() {
				return 10;
			}
		},
		/**
		 * 已经完成
		 */
		Finished {
			@Override
			public int getValue() {
				return 20;
			}
		},
		/**
		 * 已删除
		 */
		Deleted {
			@Override
			public int getValue() {
				return 30;
			}
		};
		public abstract int getValue();

		public static RiddingStatus genRiddingStatus(int t) {
			for (RiddingStatus status : RiddingStatus.values()) {
				if (status.getValue() == t)
					return status;
			}
			return null;
		}
	}

	/**
	 * 骑行类型
	 * 
	 * @author apple
	 * 
	 */
	public enum RiddingType {
		/**
		 * 远途
		 */
		Farway {
			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 短途
		 */
		ShortWay {
			@Override
			public int getValue() {
				return 10;
			}
		};
		public abstract int getValue();

		public static RiddingType genRiddingType(int t) {
			for (RiddingType type : RiddingType.values()) {
				if (type.getValue() == t)
					return type;
			}
			return null;
		}
	}

	public int getRiddingStatus() {
		return riddingStatus;
	}

	public void setRiddingStatus(int riddingStatus) {
		this.riddingStatus = riddingStatus;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLeaderUserId() {
		return leaderUserId;
	}

	public void setLeaderUserId(long leaderUserId) {
		this.leaderUserId = leaderUserId;
	}

	public long getMapId() {
		return mapId;
	}

	public void setMapId(long mapId) {
		this.mapId = mapId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRiddingType() {
		return riddingType;
	}

	public void setRiddingType(int riddingType) {
		this.riddingType = riddingType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.setCreateTimeStr(TimeUtil.getFormatTimeInMinute(createTime));
		this.createTime = createTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isLarger() {
		return isLarger;
	}

	public void setLarger(boolean isLarger) {
		this.isLarger = isLarger;
	}

	public int getIsRecom() {
		return isRecom;
	}

	public void setIsRecom(int isRecom) {
		this.isRecom = isRecom;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}

	//
	// public String getFirstPicUrl() {
	// return firstPicUrl;
	// }
	//
	// public void setFirstPicUrl(String firstPicUrl) {
	// this.firstPicUrl = firstPicUrl;
	// }

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public int getCareCount() {
		return careCount;
	}

	public void setCareCount(int careCount) {
		this.careCount = careCount;
	}

	public int getPictureCount() {
		return pictureCount;
	}

	public void setPictureCount(int pictureCount) {
		this.pictureCount = pictureCount;
	}

	public List<RiddingPicture> getRiddingPictureList() {
		return riddingPictureList;
	}

	public void setRiddingPictureList(List<RiddingPicture> riddingPictureList) {
		this.riddingPictureList = riddingPictureList;
	}

	public Profile getLeaderProfile() {
		return leaderProfile;
	}

	public void setLeaderProfile(Profile leaderProfile) {
		this.leaderProfile = leaderProfile;
	}

	public int getIsSyncSina() {
		return isSyncSina;
	}

	public void setIsSyncSina(int isSyncSina) {
		this.isSyncSina = isSyncSina;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Public getaPublic() {
		return aPublic;
	}

	public void setaPublic(Public aPublic) {
		this.aPublic = aPublic;
	}

	public int getUserSyncWifi() {
		return userSyncWifi;
	}

	public void setUserSyncWifi(int userSyncWifi) {
		this.userSyncWifi = userSyncWifi;
	}

}

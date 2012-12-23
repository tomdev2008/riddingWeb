package com.ridding.meta;

import java.io.Serializable;
import java.util.List;

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
	 * 第一张图的url
	 */
	public String firstPicUrl;
	/**
	 * 距离
	 */
	public int distance;
	/**
	 * 权重，不存数据库
	 */
	private int weight;
	/**
	 * 骑行图片列表
	 */
	private List<RiddingPicture> riddingPictureList;
	/**
	 * 队长的profile
	 */
	private Profile leaderProfile;

	/**
	 *骑行状态
	 * 
	 * @author apple
	 * 
	 */
	public enum RiddingStatus {
		/**
		 * 还没开始
		 */
		NotBeginning {
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 正在进行
		 */
		Beginning {
			public int getValue() {
				return 10;
			}
		},
		/**
		 * 已经完成
		 */
		Finished {
			public int getValue() {
				return 20;
			}
		},
		/**
		 * 已删除
		 */
		Deleted {
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

	public String getFirstPicUrl() {
		return firstPicUrl;
	}

	public void setFirstPicUrl(String firstPicUrl) {
		this.firstPicUrl = firstPicUrl;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
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

}

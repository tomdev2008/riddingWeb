package com.ridding.meta;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-12-2 下午11:36:11 Class Description
 */
public class Public {
	/**
	 * id
	 */
	private long id;
	/**
	 * 类型
	 */
	private int type;
	/**
	 * json数据
	 */
	private String json;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 上次更新时间
	 */
	private long lastUpdateTime;
	/**
	 * 权重
	 */
	private int weight;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
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

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 *广告类型
	 * 
	 * @author apple
	 * 
	 */
	public enum PublicType {
		/**
		 * 广场推荐
		 */
		PublicRecom {

			public int getValue() {
				return 1;
			}

			public Ridding getRidding(String json) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				Ridding ridding = new Ridding();
				ridding.setId(jsonObject.getLong("riddingId"));
				ridding.setLeaderUserId(jsonObject.getLong("userId"));
				
				if (jsonObject.get("firstPicUrl")!=null) {
					ridding.setFirstPicUrl(jsonObject.getString("firstPicUrl"));
				}

				return ridding;
			}

			public String setJson(long userId, long riddingId, String firstPicUrl) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("riddingId", riddingId);
				jsonObject.put("userId", userId);
				if (!StringUtils.isEmpty(firstPicUrl)) {
					jsonObject.put("firstPicUrl", firstPicUrl);
				}
				return jsonObject.toString();
			}
		};
		public abstract int getValue();

		public abstract Ridding getRidding(String json);

		public abstract String setJson(long userId, long riddingId, String firstPicUrl);

		public static PublicType genPublicType(int t) {
			for (PublicType type : PublicType.values()) {
				if (type.getValue() == t)
					return type;
			}
			return null;
		}
	}
}

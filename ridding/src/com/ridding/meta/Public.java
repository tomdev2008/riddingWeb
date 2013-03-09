package com.ridding.meta;

import com.ridding.constant.returnCodeConstance;

import net.sf.json.JSONObject;

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
	/**
	 * 骑行id
	 */
	private long riddingId;
	/**
	 * 封面url
	 */
	private String firstPicUrl = "";
	/**
	 * 广告内容类型
	 */
	private int adContentType = PublicContentType.PublicNone.getValue();
	/**
	 * 文本内容
	 */
	private String adText = "";
	/**
	 * 链接
	 */
	private String linkUrl = "";
	/**
	 * 广告图片url
	 */
	private String adImageUrl = "";

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
		JSONObject jsonObject = JSONObject.fromObject(this.json);
		if (jsonObject == null) {
			return null;
		}
		this.riddingId = jsonObject.getLong("riddingid");
		this.firstPicUrl = jsonObject.getString("firstpicurl");
		this.adContentType = jsonObject.getInt("adcontenttype");
		this.adText = jsonObject.getString("adtext");
		this.linkUrl = jsonObject.getString("linkurl");
		this.adImageUrl = jsonObject.getString("adimageurl");
		return jsonObject.toString();
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
	 * 广告类型
	 * 
	 * @author apple
	 * 
	 */
	public enum PublicType {
		NotPublicRecom {
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 广场推荐
		 */
		PublicRecom {

			@Override
			public int getValue() {
				return 1;
			}
		};
		public abstract int getValue();

		public static PublicType genPublicType(int t) {
			for (PublicType type : PublicType.values()) {
				if (type.getValue() == t)
					return type;
			}
			return null;
		}
	}

	/**
	 * 广告类型
	 * 
	 * @author apple
	 * 
	 */
	public enum PublicContentType {
		/**
		 * 什么都不是
		 */
		PublicNone {

			@Override
			public int getValue() {
				return 0;
			}
		},
		/**
		 * 文字链接
		 */
		PublicText {

			@Override
			public int getValue() {
				return 1;
			}
		},
		/**
		 * 图片链接
		 */
		PublicImage {

			@Override
			public int getValue() {
				return 2;
			}
		};

		public abstract int getValue();

		public static PublicContentType genPublicContentType(int t) {
			for (PublicContentType type : PublicContentType.values()) {
				if (type.getValue() == t)
					return type;
			}
			return null;
		}
	}

	public long getRiddingId() {
		return riddingId;
	}

	public void setRiddingId(long riddingId) {
		this.riddingId = riddingId;
	}

	public String getFirstPicUrl() {
		return firstPicUrl;
	}

	public void setFirstPicUrl(String firstPicUrl) {
		this.firstPicUrl = firstPicUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getAdImageUrl() {
		return adImageUrl;
	}

	public void setAdImageUrl(String adImageUrl) {
		this.adImageUrl = adImageUrl;
	}

	public int getAdContentType() {
		return adContentType;
	}

	public void setAdContentType(int adContentType) {
		this.adContentType = adContentType;
	}

	public String getAdText() {
		return adText;
	}

	public void setAdText(String adText) {
		this.adText = adText;
	}

	public void genJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("riddingid", this.riddingId);
		jsonObject.put("firstpicurl", this.firstPicUrl);
		jsonObject.put("adcontenttype", this.adContentType);
		jsonObject.put("linkurl", this.linkUrl);
		jsonObject.put("adimageurl", this.adImageUrl);
		jsonObject.put("adtext", this.adText);
		this.setJson(jsonObject.toString());
	}
}

package com.ridding.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ridding.constant.SystemConst;
import com.ridding.meta.vo.Location;
import com.ridding.util.StringUtil;

import net.sf.json.JSONArray;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime 2012-3-19 12:29:06 Class Description 地图meta
 */
public class IMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5226745910755824777L;
	/**
	 * id
	 */
	private long id;
	/**
	 * 地图url
	 */
	private String mapUrl = "";
	/**
	 * 地图标签
	 */
	private String mapTaps = "";
	/**
	 * 地图经纬度列表
	 */
	private String mapPoint = "";
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 对象id
	 */
	private long objectId;
	/**
	 * 记录的来源
	 */
	private int objectType;
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 地图状态
	 */
	private int status;
	/**
	 * 地图距离
	 */
	private int distance;
	/**
	 * 起始位置
	 */
	private String beginLocation = "";
	/**
	 * 结束位置
	 */
	private String endLocation = "";
	/**
	 * 路过位置
	 */
	private String midLocation = "";
	/**
	 * 路过位置列表
	 */
	private List<String> midLocationList;
	/**
	 * 起点的城市id
	 */
	private long cityId;
	/**
	 * 城市名称
	 */
	private String cityName;
	/**
	 * 中点位置
	 */
	private Location centerLocation;
	/**
	 * 地图的封面图片
	 */
	private long avatorPic;
	/**
	 * 头图url
	 */
	private String avatorPicUrl;
	/**
	 * 没有使用
	 */
	public static int NotUsing = 0;
	/**
	 * 正在使用
	 */
	public static int Using = 1;
	/**
	 * url对应的key
	 */
	private String urlKey;
	

	public String getBeginLocation() {
		return beginLocation;
	}

	public void setBeginLocation(String beginLocation) {
		this.beginLocation = beginLocation;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getMapPoint() {
		return mapPoint;
	}

	public void setMapPoint(String mapPoint) {
		this.mapPoint = mapPoint;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userid) {
		this.userId = userid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public String getMapTaps() {
		return mapTaps;
	}

	public void setMapTaps(String mapTaps) {
		this.mapTaps = mapTaps;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getMidLocation() {
		return midLocation;
	}

	public void setMidLocation(String midLocation) {
		this.midLocation = midLocation;
	}

	public List<String> getMidLocationList() {
		JSONArray jsonArray = new JSONArray().fromObject(this.midLocation);
		if (jsonArray == null) {
			return null;
		}
		List<String> list = new ArrayList<String>(jsonArray.size());
		for (Object object : jsonArray) {
			list.add(object.toString());
		}
		this.midLocationList = list;
		return list;
	}

	public void setMidLocationList(List<String> midLocationList) {
		this.midLocationList = midLocationList;
	}

	public void setCenterLocation(Location centerLocation) {
		this.centerLocation = centerLocation;
	}

	public long getAvatorPic() {
		return avatorPic;
	}

	public void setAvatorPic(long avatorPic) {
		this.avatorPic = avatorPic;
	}

	/**
	 * 得到中点位置
	 * 
	 * @return
	 */
	public void getCenterLocation() {
		String trimMapTaps = this.mapTaps;
		if (this.mapTaps.startsWith("\"")) {
			trimMapTaps = mapTaps.substring(1, mapTaps.length() - 1);
		}
		JSONArray jsonArray = new JSONArray().fromObject(trimMapTaps);
		if (jsonArray == null) {
			return;
		}
		List<String> latitudes = new ArrayList<String>(jsonArray.size());
		List<String> longtitudes = new ArrayList<String>(jsonArray.size());
		for (Object object : jsonArray) {
			String[] splits = object.toString().split(",");
			latitudes.add(splits[0]);
			longtitudes.add(splits[1]);
		}
		Location location = new Location();
		double l = 0.0;
		for (String latitude : latitudes) {
			l = l + Double.valueOf(latitude);
		}
		location.setLatitude(l / latitudes.size());
		l = 0.0;
		for (String longtitude : longtitudes) {
			l = l + Double.valueOf(longtitude);
		}
		location.setLongtitude(l / longtitudes.size());
		this.centerLocation = location;
	}

	/**
	 * 得到静态地图信息
	 * 
	 * @param staticImgSrc
	 * @return
	 */
	public String getStaticImgSrc() {
		if (StringUtils.isEmpty(this.mapTaps)) {
			return null;
		}
		String trimMapTaps = this.mapTaps;
		if (this.mapTaps.startsWith("\"")) {
			trimMapTaps = mapTaps.substring(1, mapTaps.length() - 1);
		}

		JSONArray jsonArray = new JSONArray().fromObject(trimMapTaps);
		if (jsonArray == null) {
			return null;
		}
		this.getCenterLocation();
		String markers = "";
		int index = 0;
		for (Object object : jsonArray) {
			if (index == 0) {
				markers = markers + "&markers=icon:" + SystemConst.getValue("HOST") + "/img/mapCreate/startPoint.png%7C" + object.toString();
			} else if (index == jsonArray.size() - 1) {
				markers = markers + "&markers=icon:" + SystemConst.getValue("HOST") + "/img/mapCreate/endPoint.png%7C" + object.toString();
			} else {
				markers = markers + "&markers=color:blue%7Clabel:M%7C" + object.toString();
			}
			index++;
		}
		return "http://maps.google.com/maps/api/staticmap?center=" + this.centerLocation.getLatitude() + "," + this.centerLocation.getLongtitude()
				+ "&size=260x260&sensor=false&maptype=roadmap" + markers;
	}

	/**
	 * ftl需要
	 * 
	 * @return
	 */
	public List<String> getMapTapsList() {
		JSONArray jsonArray = new JSONArray().fromObject(this.mapTaps);
		if (jsonArray == null) {
			return null;
		}
		List<String> mapTapsList = new ArrayList<String>(jsonArray.size());
		for (Object object : jsonArray) {
			mapTapsList.add(object.toString());
		}
		return mapTapsList;
	}

	/**
	 * ftl需要
	 * 
	 * @return
	 */
	public double[] getCenterLocations() {
		double[] doubles = new double[2];
		doubles[0] = this.centerLocation.getLatitude();
		doubles[1] = this.centerLocation.getLongtitude();
		return doubles;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public String getAvatorPicUrl() {
		return avatorPicUrl;
	}

	public void setAvatorPicUrl(String avatorPicUrl) {
		this.avatorPicUrl = avatorPicUrl;
	}

}

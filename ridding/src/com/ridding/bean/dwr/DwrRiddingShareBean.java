package com.ridding.bean.dwr;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ridding.meta.Ridding;
import com.ridding.meta.Ridding.RiddingStatus;
import com.ridding.service.RiddingService;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-9-7 上午01:25:51 Class Description
 */
@Service("dwrRiddingShareBean")
public class DwrRiddingShareBean {

	@Resource
	private RiddingService riddingService;

	/**
	 * 使用别人的骑行活动
	 * 
	 * @param riddingId
	 * @return
	 */
	public boolean useOthersRidding(long riddingId,long userId) {
		Ridding ridding = riddingService.getRidding(riddingId);
		if (ridding == null) {
			return false;
		}
		Ridding newRidding = new Ridding();
		long nowTime = new Date().getTime();
		newRidding.setLeaderUserId(userId);
		newRidding.setCreateTime(nowTime);
		newRidding.setLastUpdateTime(nowTime);
		newRidding.setMapId(ridding.getMapId());
		newRidding.setName(ridding.getName());
		newRidding.setRiddingStatus(RiddingStatus.Beginning.getValue());
		newRidding.setRiddingType(0);
		newRidding.setUserCount(1);
		return riddingService.addRidding(newRidding) != null;
	}
}

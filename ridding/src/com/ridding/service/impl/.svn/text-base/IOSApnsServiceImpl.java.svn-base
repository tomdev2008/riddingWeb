package com.ridding.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javapns.Push;
import javapns.notification.PushNotificationPayload;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ridding.mapper.IosApnsMapper;
import com.ridding.meta.ApnsDevice;
import com.ridding.service.IOSApnsService;
import com.ridding.util.ListUtils;

/**
 * @author zhengyisheng E-mail:zhengyisheng@gmail.com
 * @version CreateTime：2012-8-1 下午06:26:11 Class Description
 */
@Service("iosApnsService")
public class IOSApnsServiceImpl implements IOSApnsService {

	private static final String PASSWORD = "13823381398";
	// developerApns.p12
	private static final String FILENAME = "aps_product_identity.p12";

	private static final int THREAD = 10;

	private static final String MESSAGENAME = "message";

	private static final Logger logger = Logger.getLogger(IOSApnsServiceImpl.class);

	@Resource
	private IosApnsMapper iosApnsMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.IOSApnsService#sendApns(java.lang.String)
	 */
	public void sendApns(String text) {
		List<ApnsDevice> list = iosApnsMapper.getAllApnsDevice();
		if (!ListUtils.isEmptyList(list)) {
			this.sendOneMessage(list, "message", text, "message");
		}
	}

	/**
	 * ios群相册发消息
	 * 
	 * @param devices
	 *            用户的token
	 * @param messageName
	 *            消息名称
	 * @param message
	 *            消息体
	 * @param title
	 *            alert的标题
	 * @return 返回成功数量
	 */
	public int sendOneMessage(List<ApnsDevice> devices, String messageName, String message, String title) {
		Integer totalCount = 0;
		try {
			JSONObject jsonObject = new JSONObject(message);
			totalCount = (Integer) jsonObject.get("totalCount");
		} catch (JSONException e2) {
			logger.info("sendOneMessage get totalCount error where totalCount=" + totalCount);
		}
		for (ApnsDevice device : devices) {
			PushNotificationPayload payload = PushNotificationPayload.complex();
			try {
				payload.addCustomAlertBody(title);
				payload.addBadge(totalCount);
				payload.addCustomAlertActionLocKey("查看");
				File resourceFile = ResourceUtils.getFile("classpath:" + FILENAME);
				payload.addCustomDictionary(messageName, message);
				// true表示在production环境
				Push.alert(message, (Object) resourceFile, PASSWORD, true, device);
				logger.info("success send apsn where userid=" + device.getUserId());
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return devices.size(); // 还没做异常处理
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.IOSApnsService#addIosApns(com.ridding.meta.ApnsDevice
	 * )
	 */
	@Override
	public boolean addIosApns(ApnsDevice apnsDevice) {
		ApnsDevice apnsDevice2 = iosApnsMapper.getApnsDevice(apnsDevice.getUserId());
		if (apnsDevice2 != null) {
			return iosApnsMapper.updateApns(apnsDevice) > 0;
		}
		return iosApnsMapper.addApnsDevice(apnsDevice) > 0;
	}
}

package com.ridding.service.impl;

import java.io.File;
import java.util.List;

import javapns.Push;
import javapns.notification.PushNotificationPayload;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.JSONException;
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
	// developerApns.p12  aps_product_identity.p12
	private static final String FILENAME = "aps_product_identity.p12";

	private static final int THREAD = 10;

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
			this.sendMessages(list, "message", text, "message");
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
	public int sendMessages(List<ApnsDevice> devices, String messageName, String message, String title) {
		for (ApnsDevice device : devices) {
			this.sendOneMessages(device, messageName, message, title);
		}
		return devices.size(); // 还没做异常处理
	}

	/**
	 * 发送一条信息
	 * 
	 * @param device
	 * @param messageName
	 * @param message
	 * @param title
	 * @return
	 */
	public boolean sendOneMessages(ApnsDevice device, String messageName, String message, String title) {
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomAlertBody(title);
			// payload.addBadge(totalCount);
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
		return true;
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
			apnsDevice2.setToken(apnsDevice.getToken());
			apnsDevice2.setLastUpdateTime(apnsDevice.getLastUpdateTime());
			apnsDevice2.setVersion(apnsDevice.getVersion());
			return iosApnsMapper.updateApns(apnsDevice2) > 0;
		}
		return iosApnsMapper.addApnsDevice(apnsDevice) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.IOSApnsService#sendUserCommentApns(long,
	 * java.lang.String, long)
	 */
	@Override
	public void sendUserApns(long userId, String message) {
		ApnsDevice device = iosApnsMapper.getApnsDevice(userId);
		if (device != null) {
			this.sendOneMessages(device, "消息", message, "消息");
		}

	}
}

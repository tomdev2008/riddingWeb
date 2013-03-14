package com.ridding.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

	private static final String PASSWORD = "138233";
	// developerApns.p12 aps_product_identity.p12 推送的p12是直接用证书到处，不是用key导出
	private static final String FILENAME = "aps_product_identity.p12";

	private static final String HOST = "gateway.push.apple.com";

	private static final Logger logger = Logger.getLogger(IOSApnsServiceImpl.class);

	@Resource
	private IosApnsMapper iosApnsMapper;

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.IOSApnsService#sendApns(java.lang.String)
	 */
	public void sendApns(String text, long userId, String version) {

		List<ApnsDevice> list = null;
		if (userId <= 0) {
			list = iosApnsMapper.getAllApnsDevice();
		} else {
			list = new ArrayList<ApnsDevice>(1);
			ApnsDevice apns = iosApnsMapper.getApnsDevice(userId);
			list.add(apns);
		}

		if (!ListUtils.isEmptyList(list)) {
			this.sendMessages(list, "消息", text, "消息", version);
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
	public int sendMessages(List<ApnsDevice> devices, String messageName, String message, String title, String version) {
		for (ApnsDevice device : devices) {
			if (!StringUtils.isEmpty(version)) {
				if (device.getVersion().equals(version)) {
					continue;
				}
			}
			this.asyncSendOneMessages(device, messageName, message, title);
		}
		return devices.size(); // 还没做异常处理
	}

	/**
	 * 异步发消息
	 * 
	 * @param profile
	 * @return
	 */
	public boolean asyncSendOneMessages(final ApnsDevice device, final String messageName, final String message, final String title) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("asyncSendOneMessages beginSend where device userId=" + device.getUserId() + " message=" + message);
					PayLoad payLoad = new PayLoad();
					payLoad.addAlert(message);
					payLoad.addSound("default");

					PushNotificationManager pushManager = PushNotificationManager.getInstance();
					pushManager.addDevice("iPhone", device.getToken());
					File resourceFile = ResourceUtils.getFile("classpath:" + FILENAME);
					pushManager.initializeConnection(HOST, 2195, resourceFile.getPath(), PASSWORD, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);

					// Send Push
					Device client = pushManager.getDevice("iPhone");
					pushManager.sendNotification(client, payLoad);
					pushManager.stopConnection();

					pushManager.removeDevice("iPhone");
					logger.info("asyncSendOneMessages finish where device userId=" + device.getUserId() + " message=" + message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
			this.asyncSendOneMessages(device, "消息", message, "消息");
		}

	}
}

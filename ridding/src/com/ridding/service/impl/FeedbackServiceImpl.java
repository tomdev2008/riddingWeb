package com.ridding.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ridding.mapper.FeedbackMapper;
import com.ridding.mapper.ProfileMapper;
import com.ridding.meta.Feedback;
import com.ridding.meta.Profile;
import com.ridding.service.FeedbackService;
import com.ridding.util.MailSenderInfo;
import com.ridding.util.SimpleMailSenderUtil;

/**
 * @author yunshang_734 E-mail:yunshang_734@163.com
 * @version CreateTime：2013-3-1 下午10:31:23 Class Description
 */
@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {
	private static final Logger logger = Logger
			.getLogger(FeedbackServiceImpl.class);

	@Resource
	private FeedbackMapper feedbackMapper;

	@Resource
	private ProfileMapper profileMapper;

	private static ExecutorService executorService = Executors
			.newCachedThreadPool();

	public boolean asyncgrayMailAvator(final long userId, final long userQQ,
			final String userMail, final String description,
			final String deviceVersion, final String version,
			final String appVersion) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					MailSenderInfo mailSenderInfo = new MailSenderInfo();
					mailSenderInfo.setMailServerHost("smtp.163.com");
					mailSenderInfo.setMailServerPort("25");
					mailSenderInfo.setValidate(true);
					mailSenderInfo.setUserName("zyslovely@163.com");
					mailSenderInfo.setPassword("zyslovely138233");
					mailSenderInfo.setFromAddress("zyslovely@163.com");
					mailSenderInfo.setToAddress("riddingapp@gmail.com");
					mailSenderInfo.setSubject("Feedback with userId = "
							+ userId);
					mailSenderInfo.setContent("userQQ = " + userQQ
							+ ",userMail = " + userMail + ",description = "
							+ description + ",deviceVersion = " + deviceVersion
							+ ",version = " + version + ",appVersion = "
							+ appVersion);
					SimpleMailSenderUtil simpleMailSenderUtil = new SimpleMailSenderUtil();
					simpleMailSenderUtil.sendTextMail(mailSenderInfo);
				} catch (Exception e) {
					logger.error("FeedbackServiceImpl asyncgrayMailAvator where =");
					e.printStackTrace();
					return;
				}
			}
		});
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ridding.service.FeedbackService#addFeedback(com.ridding.meta.Feedback
	 * )
	 */
	@Override
	public boolean addFeedback(long userId, long userQQ, String userMail,
			String description, String deviceVersion, String version,
			String appVersion) {
		if (userId < 0) {
			logger.error("Error to add feedback with uerId = " + userId);
			return false;
		}
		Feedback feedback = new Feedback();
		feedback.setUserId(userId);
		feedback.setUserQQ(userQQ);
		feedback.setUserMail(userMail);
		feedback.setDescription(description);
		feedback.setCreateTime(new Date().getTime());
		feedback.setStatus(Feedback.FeedbackStatus.Undone.getValue());
		feedback.setReplyTime(-1);
		feedback.setReply("");
		feedback.setDeviceVersion(deviceVersion);
		feedback.setVersion(version);
		feedback.setAppVersion(appVersion);
		this.asyncgrayMailAvator(userId, userQQ, userMail, description,
				deviceVersion, version, appVersion);
		if (feedbackMapper.addFeedback(feedback) > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.FeedbackService#getFeedbackList()
	 */
	public List<Feedback> getFeedbackList() {
		return feedbackMapper.getFeedbackList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ridding.service.FeedbackService#replyFeedback(long, long,
	 * java.lang.String)
	 */
	public boolean replyFeedback(long id, long userId, String reply) {
		Profile profile = profileMapper.getProfile(userId);
		if (profile.getLevel() != 1) {
			logger.error("Error to reply feedback with userId = " + userId
					+ " which is not a manager's id!");
			return false;
		}
		long nowTime = new Date().getTime();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("reply", reply);
		map.put("replyTime", nowTime);
		map.put("status", Feedback.FeedbackStatus.Done.getValue());
		if (feedbackMapper.updateFeedback(map) > 0) {
			return true;
		}
		return false;
	}
}

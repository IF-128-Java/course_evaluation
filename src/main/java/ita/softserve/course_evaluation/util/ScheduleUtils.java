package ita.softserve.course_evaluation.util;


import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import ita.softserve.course_evaluation.service.NotificationService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ScheduleUtils {
	
	private final FeedbackRequestService feedbackRequestService;
	private final UserService userService;
	private final NotificationService notificationService;
	
	public ScheduleUtils(FeedbackRequestService feedbackRequestService, UserService userService, NotificationService notificationService) {
		this.feedbackRequestService = feedbackRequestService;
		this.userService = userService;
		this.notificationService = notificationService;
	}
	@Transactional
//	@Scheduled(cron = "0 0 12 * * *")
	public void sendEmailNotification() {
		List<FeedbackRequestDto> feedbackRequests = feedbackRequestService.findAllByStatusActiveAndValidDate();
		feedbackRequests.forEach(this::sendNotificationToAvailableUsers);
	}
	
	private void sendNotificationToAvailableUsers(FeedbackRequestDto fbr) {
		List<UserDto> users = userService.getAllStudentsByFeedbackRequestIdWithoutFeedback(fbr.getId());
		notificationService.sendNotificationToAvailableUsers(users, fbr.getId());
		feedbackRequestService.changeStatus(fbr, FeedbackRequestStatus.SENT.ordinal());
	}
}

package ita.softserve.course_evaluation.util;


import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import ita.softserve.course_evaluation.service.NotificationService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.data.domain.Pageable;
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
	@Scheduled(cron = "0 0 12 * * *")
	public void sendEmailNotification() {
		List<FeedbackRequestDto> feedbackRequests = feedbackRequestService.findAllByStatusAndValidDate(FeedbackRequestStatus.SENT.ordinal());
		feedbackRequests.forEach(this::sendNotificationToAvailableUsers);
	}
	
	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void checkFeedbackRequestByDateToArchive() {
		feedbackRequestService.findAllByExpiredDateAndSetStatusToArchive(2);
	}
	
	private void sendNotificationToAvailableUsers(FeedbackRequestDto fbr) {
		List<UserDto> users = userService.getAllStudentsByFeedbackRequestIdWithoutFeedback(Pageable.unpaged(),fbr.getId()).getContent();
		notificationService.sendNotificationToAvailableUsers(users, fbr.getId());
		feedbackRequestService.changeStatusAndLastNotification(fbr, FeedbackRequestStatus.SENT.ordinal());
	}
}

package ita.softserve.course_evaluation.util;


import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import ita.softserve.course_evaluation.service.NotificationService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
		List<FeedbackRequestDto> feedbackRequests = feedbackRequestService.findAllByStatusActiveAndValidDate(FeedbackRequestStatus.SENT.ordinal());
		feedbackRequests.forEach(fbr -> {
			LocalDateTime scheduleDate = fbr.getStartDate().plusDays(3);
			boolean isFirstNotification = fbr.getLastNotification()==null;
			boolean isSecondNotification = scheduleDate.isBefore(LocalDateTime.now()) && scheduleDate.isAfter(fbr.getLastNotification());
			if(isFirstNotification || isSecondNotification){
				sendNotificationToAvailableUsers(fbr);
			}
		});
	}
	
	private void sendNotificationToAvailableUsers(FeedbackRequestDto fbr) {
		List<UserDto> users = userService.getAllStudentsByFeedbackRequestIdWithoutFeedback(fbr.getId());
		notificationService.sendNotificationToAvailableUsers(users, fbr.getId());
		feedbackRequestService.changeStatusAndLastNotification(fbr, FeedbackRequestStatus.SENT.ordinal());
	}
}

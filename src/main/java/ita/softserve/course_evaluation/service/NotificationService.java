package ita.softserve.course_evaluation.service;

public interface NotificationService {
	void sendNotificationToUser(String email, Long id);
}

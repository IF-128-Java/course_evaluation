package ita.softserve.course_evaluation.service;

import java.util.List;

public interface NotificationService {
	
	void sendNotificationToUser(String email, Long id);
	void sendNotificationToAvailableUsers(List<String> emails, Long id);
}

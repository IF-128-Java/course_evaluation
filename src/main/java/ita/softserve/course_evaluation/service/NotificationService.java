package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UserDto;

import java.util.List;

public interface NotificationService {
	
	void sendNotificationToUser(String email, Long id);
	void sendNotificationToAvailableUsers(List<UserDto> users, Long id);
}

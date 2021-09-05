package ita.softserve.course_evaluation.service.mail.context;

import ita.softserve.course_evaluation.entity.User;

public class NotificationFeedbackRequestMessageContext extends AbstractEmailContext{
	
	@Override
	public <T> void init(T context) {
		User user = (User) context;
		put("userName", user.getFirstName() + " " + user.getLastName());
		setTemplateLocation("emails/feedback-request-notification");
		setSubject("Feedback request available");
		setFrom("noreply.courseevaluation@gmail.com");
		setTo(user.getEmail());
	}
}

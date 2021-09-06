package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.CourseService;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import ita.softserve.course_evaluation.service.NotificationService;
import ita.softserve.course_evaluation.service.mail.EmailService;
import ita.softserve.course_evaluation.service.mail.context.NotificationFeedbackRequestMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
	@Value("${site.base.url.https}")
	private String siteUrl;
	
	private final UserRepository userRepository;
	private final EmailService emailService;
	private final FeedbackRequestService feedbackRequestService;
	private final CourseService courseService;
	
	public NotificationServiceImpl(UserRepository userRepository, EmailService emailService, FeedbackRequestService feedbackRequestService, CourseService courseService) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.feedbackRequestService = feedbackRequestService;
		this.courseService = courseService;
	}
	
	
	@Override
	public void sendNotificationToUser(String email, Long id) {
		NotificationFeedbackRequestMessageContext emailContext = new NotificationFeedbackRequestMessageContext();
		Optional<User> user = userRepository.findUserByEmail(email);
		
		if(user.isPresent()){
			emailContext.init(user.get());
			FeedbackRequest feedbackRequest = FeedbackRequestDtoMapper.fromDto(feedbackRequestService.getFeedbackRequestById(id));
			String courseName = courseService.getById(feedbackRequest.getCourse().getId()).getCourseName();
			emailContext.setSubject("Feedback request to " +  courseName + " available");
			emailContext.put("FeedbackRequestDescription", feedbackRequest.getFeedbackDescription());
			emailContext.put("CourseName", courseName);
			emailContext.put("siteUrl", siteUrl);
			LocalDate date = feedbackRequest.getStartDate().toLocalDate();
			LocalDate now = LocalDate.now();
			if(now.isAfter(date)){
				long passDays = ChronoUnit.DAYS.between(date,now);
				emailContext.put("inform", "let you know that a feedback request is waiting for your response within " + passDays +" days");
			} else {
				emailContext.put("inform", "inform you that you have an active feedback request ");
			}
			try {
				emailService.sendMail(emailContext);
			} catch (MessagingException e) {
				e.printStackTrace();
				log.error("Error on send email. Message - {}", e.getMessage());
			}
			
		}
		else{
			throw new UsernameNotFoundException("User doesn't exists");
		}
	}
	
	@Override
	public void sendNotificationToAvailableUsers(List<UserDto> emails, Long id) {
		emails.forEach(email -> sendNotificationToUser(email.getEmail(), id));
		FeedbackRequestDto feedbackRequestDto = feedbackRequestService.getFeedbackRequestById(id);
		feedbackRequestService.changeStatusAndLastNotification(feedbackRequestDto, FeedbackRequestStatus.SENT.ordinal());
	}
}

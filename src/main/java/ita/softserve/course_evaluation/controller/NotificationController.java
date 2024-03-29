package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.service.NotificationService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Api(tags = "Notification service REST API")
@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {
	private final UserService userService;
	private final NotificationService notificationService;
	
	public NotificationController(UserService userService, NotificationService notificationService) {
		this.userService = userService;
		this.notificationService = notificationService;
		
	}
	
	@GetMapping("/fbrequest/{id}/students")
	public ResponseEntity<Page<UserDto>> getAllStudentsByFeedbackRequestId(@RequestParam int page, @RequestParam int size, @PathVariable long id) {
		return Objects.isNull(userService.getAllStudentsByFeedbackRequestIdWithoutFeedback(PageRequest.of(page, size),id)) ?
				       ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
				       ResponseEntity.status(HttpStatus.OK).body(userService.getAllStudentsByFeedbackRequestIdWithoutFeedback(PageRequest.of(page, size),id));
	}
	
	@PostMapping("/fbrequest/{id}/student")
	public ResponseEntity<?> sendNotificationOnEmailToStudent(@PathVariable long id, @RequestBody String email){
		notificationService.sendNotificationToUser(email, id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@PostMapping("/fbrequest/{id}/students")
	public ResponseEntity<?> sendNotificationOnEmailToAllStudent(@PathVariable long id, @RequestBody List<UserDto> users){
		notificationService.sendNotificationToAvailableUsers(users, id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
}

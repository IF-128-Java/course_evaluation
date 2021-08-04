package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.service.FeedbackService;
import ita.softserve.course_evaluation.api.FeedbackApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/feedback")
public class FeedbackController implements FeedbackApi {
	
	private final FeedbackService feedbackService;
	
	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('READ')")
	public ResponseEntity<FeedbackDto> createFeedback(@RequestBody FeedbackDto dto) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackService.create(dto));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('WRITE')")
	public ResponseEntity<FeedbackDto> getFeedback(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackService.getFeedbackById(id));
	}
}

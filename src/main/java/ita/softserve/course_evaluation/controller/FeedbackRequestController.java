package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/feedback_request")
public class FeedbackRequestController {
	
	private final FeedbackRequestService feedbackRequestService;
	
	public FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
		this.feedbackRequestService = feedbackRequestService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void createFeedback(@RequestBody FeedbackRequestDto dto) {
		feedbackRequestService.create(dto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FeedbackRequestDto> getFeedback(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.getFeedbackById(id));
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void editFeedback(@RequestBody FeedbackRequestDto dto, @PathVariable Long id) {
		dto.setId(id);
		feedbackRequestService.update(dto);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteFeedback(@PathVariable Long id) {
		feedbackRequestService.delete(id);
	}
}

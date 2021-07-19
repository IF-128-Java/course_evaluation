package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.service.FeedbackService;
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
@RequestMapping("api/v1/feedback")
public class FeedbackController {
	
	private final FeedbackService feedbackService;
	
	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void createFeedback(@RequestBody FeedbackDto dto) {
		feedbackService.create(dto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FeedbackDto> getFeedback(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackService.getFeedbackById(id));
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void editFeedback(@RequestBody FeedbackDto dto, @PathVariable Long id) {
		dto.setId(id);
		feedbackService.update(dto);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteFeedback(@PathVariable Long id) {
		feedbackService.delete(id);
	}
}

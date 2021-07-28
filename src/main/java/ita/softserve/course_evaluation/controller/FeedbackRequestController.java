package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	@PreAuthorize("hasAuthority('WRITE')")
	public ResponseEntity<FeedbackRequestDto>  createFeedbackRequest(@RequestBody FeedbackRequestDto dto) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.create(dto));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('WRITE')")
	public ResponseEntity<FeedbackRequestDto> getFeedbackRequest(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.getFeedbackRequestById(id));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('UPDATE')")
	public ResponseEntity<FeedbackRequestDto> editFeedbackRequest(@RequestBody FeedbackRequestDto dto, @PathVariable Long id) {
		dto.setId(id);
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.update(dto));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('UPDATE')")
	public void deleteFeedbackRequest(@PathVariable Long id) {
		feedbackRequestService.delete(id);
	}
}

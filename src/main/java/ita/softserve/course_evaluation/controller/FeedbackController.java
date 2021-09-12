package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Api(tags = "Feedback service REST API")
@RestController
@RequestMapping("api/v1/feedback")
public class FeedbackController {
	
	private final FeedbackService feedbackService;
	
	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@ApiOperation(value = "Create new Feedback")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = HttpStatuses.CREATED),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@PostMapping
	@PreAuthorize("hasAuthority('READ')")
	public ResponseEntity<FeedbackDto> createFeedback(@ApiParam(value = "FeedbackDto")
														  @RequestBody FeedbackDto dto) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackService.create(dto));
	}

	@ApiOperation(value = "Find Feedback by Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackDto.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
	})
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ')")
	public ResponseEntity<FeedbackDto> getFeedback(@ApiParam(value = "Feedback id. Cannot be empty")
													   @PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackService.getFeedbackById(id));
	}

	@GetMapping("/feedback_request/{id}")
	@PreAuthorize("hasAuthority('WRITE')")
	public ResponseEntity <Page<FeedbackDto>> getAllFeedbackByFeedbackRequestId(@ApiParam(value = "FeedbackRequest id. Cannot be empty")
																					@RequestParam int page, @RequestParam int size, @PathVariable Long id){
	return Objects.isNull(feedbackService.findAllByFeedbackRequestId(PageRequest.of(page, size),id)) ?
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
			ResponseEntity.status(HttpStatus.OK).body(feedbackService.findAllByFeedbackRequestId(PageRequest.of(page, size),id));
	}

	@ApiOperation(value = "Create new Feedback")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = HttpStatuses.CREATED),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@PostMapping("/student/")
	@PreAuthorize("hasAuthority('READ')")
	public ResponseEntity<FeedbackDto> addFeedback(@ApiParam(value = "FeedbackDto")
													  @RequestBody FeedbackDto dto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(feedbackService.addFeedback(dto));
	}

}

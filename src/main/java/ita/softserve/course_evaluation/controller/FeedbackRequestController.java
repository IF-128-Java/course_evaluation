package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDto;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Api(tags = "FeedbackRequest service REST API")
@RestController
@RequestMapping("api/v1/feedback_request")
public class FeedbackRequestController {
	
	private final FeedbackRequestService feedbackRequestService;
	
	public FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
		this.feedbackRequestService = feedbackRequestService;
	}
	
	@ApiOperation(value = "Create FeedbackRequest")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackRequestDto.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
	})
	@PostMapping
	@PreAuthorize("hasAuthority('UPDATE')")
	public ResponseEntity<FeedbackRequestDto> createFeedbackRequest(@RequestBody FeedbackRequestDto dto) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.create(dto));
	}
	
	@ApiOperation(value = "Get FeedbackRequest by Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackRequestDto.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@GetMapping("/{id}")
	public ResponseEntity<FeedbackRequestDto> getFeedbackRequest(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.getFeedbackRequestById(id));
	}
	
	@ApiOperation(value = "Update FeedbackRequest")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackRequestDto.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@PutMapping("/")
	@PreAuthorize("hasAuthority('UPDATE')")
	public ResponseEntity<FeedbackRequestDto> editFeedbackRequest(@RequestBody FeedbackRequestDto dto) {
		return ResponseEntity.status(HttpStatus.OK)
				       .body(feedbackRequestService.update(dto));
	}
	
	@ApiOperation(value = "Delete FeedbackRequest by Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('UPDATE')")
	public void deleteFeedbackRequest(@PathVariable Long id) {
		feedbackRequestService.delete(id);
	}
	
	@ApiOperation(value = "Get all feedbacks by course id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackRequestDto.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@GetMapping("/course/{id}")
	public ResponseEntity<Page<FeedbackRequestDto>> getAllFeedbackRequestByCourse(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
		return Objects.isNull(feedbackRequestService.findAllByCourseId(PageRequest.of(page, size), id)) ?
				       ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
				       ResponseEntity.status(HttpStatus.OK).body(feedbackRequestService.findAllByCourseId(PageRequest.of(page, size), id));
	}


	@ApiOperation(value = "Get all feedbackrequests by course id only")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@GetMapping("/student/course/{id}")
	public ResponseEntity<List<FeedbackRequestDto>> getFeedbackRequestByCourseIDOnly(@PathVariable long id) {
		return Objects.isNull(feedbackRequestService.getFeedbackRequestByCourseIdOnly(id)) ?
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
				ResponseEntity.status(HttpStatus.OK).body(feedbackRequestService.getFeedbackRequestByCourseIdOnly(id));
	}

	@ApiOperation(value = "Get all feedback requests with student id by course id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@GetMapping("/course/{idc}/student/{ids}")
	public ResponseEntity<List<StudentFeedbackRequestDto>> getFeedbackRequestByCourseIdAndStudentId(@PathVariable long idc, @PathVariable long ids) {
		return ResponseEntity.status(HttpStatus.OK).body(feedbackRequestService.getFeedbackRequestByCourseIdAndStudentId(idc, ids));
	}

}

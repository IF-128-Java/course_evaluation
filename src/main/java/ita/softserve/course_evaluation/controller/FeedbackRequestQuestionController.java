package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.service.FeedbackRequestQuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "FeedbackRequestQuestion service REST API")
@RestController
@RequestMapping("api/v1/feedback_request")
public class FeedbackRequestQuestionController {
	
	private final FeedbackRequestQuestionService feedbackRequestQuestionService;
	
	public FeedbackRequestQuestionController(FeedbackRequestQuestionService feedbackRequestQuestionService) {
		this.feedbackRequestQuestionService = feedbackRequestQuestionService;
	}
	
	@ApiOperation(value = "Get Questions by FeedbackRequestId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
			@ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
	})
	@GetMapping("/{id}/questions")
	public ResponseEntity<List<QuestionDto>> getQuestionsByFeedbackRequest(@PathVariable("id") Long feedbackRequestId) {
		final List<QuestionDto> questions = feedbackRequestQuestionService.getQuestionsByFeedbackRequest(feedbackRequestId);
		return  !CollectionUtils.isEmpty(questions)
				       ? new ResponseEntity<>(questions, HttpStatus.OK)
				       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@ApiOperation(value = "Add questions to FeedbackRequest")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
	})
	@PostMapping("/{id}/questions")
	public void addQuestionToFeedbackRequest(@PathVariable("id") Long feedbackRequestId, @RequestBody List<QuestionDto> questionIds) {
		feedbackRequestQuestionService.assignQuestion(feedbackRequestId, questionIds);
	}
}

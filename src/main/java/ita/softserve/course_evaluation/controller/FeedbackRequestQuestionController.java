package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.service.FeedbackRequestQuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/feedback_request")
public class FeedbackRequestQuestionController {
	
	private final FeedbackRequestQuestionService feedbackRequestQuestionService;
	
	public FeedbackRequestQuestionController(FeedbackRequestQuestionService feedbackRequestQuestionService) {
		this.feedbackRequestQuestionService = feedbackRequestQuestionService;
	}
	
	@GetMapping("/{id}/questions")
	public ResponseEntity<List<QuestionDto>> getQuestionsByFeedbackRequest(@PathVariable("id") Long feedbackRequestId) {
		final List<QuestionDto> questions = feedbackRequestQuestionService.getQuestionsByFeedbackRequest(feedbackRequestId);
		return questions != null && !questions.isEmpty()
				       ? new ResponseEntity<>(questions, HttpStatus.OK)
				       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/{id}/questions")
	public void addQuestionToFeedbackRequest(@PathVariable("id") Long feedbackRequestId, @RequestBody List<QuestionDto> questionIds) {
		feedbackRequestQuestionService.assignQuestion(feedbackRequestId, questionIds);
	}
}

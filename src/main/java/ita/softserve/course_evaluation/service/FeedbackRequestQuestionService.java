package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.QuestionDto;

import java.util.List;

public interface FeedbackRequestQuestionService {
	
	List<QuestionDto> getQuestionsByFeedbackRequest(Long id);
	
	void assignQuestion(Long feedbackRequestId, List<QuestionDto> questionIds);
}

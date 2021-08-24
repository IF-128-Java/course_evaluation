package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackRequestQuestionServiceImpl implements FeedbackRequestQuestionService {
	
	private final QuestionRepository questionRepository;
	private final FeedbackRequestRepository feedbackRequestRepository;
	
	public FeedbackRequestQuestionServiceImpl(QuestionRepository questionRepository, FeedbackRequestRepository feedbackRequestRepository) {
		this.questionRepository = questionRepository;
		this.feedbackRequestRepository = feedbackRequestRepository;
	}
	
	@Override
	public List<QuestionDto> getQuestionsByFeedbackRequest(Long id) {
		List<QuestionDto> questionDtoList = QuestionDtoMapper.toDto(questionRepository.findAllQuestionsByFeedbackRequest(id));
		return questionDtoList;
	}
	
	@Override
	public void assignQuestion(Long feedbackRequestId, List<QuestionDto> questions) {
		Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepository.findById(feedbackRequestId);
		if (feedbackRequest.isPresent()) {
			FeedbackRequest feedbackRequestDb = feedbackRequest.get();
			feedbackRequestDb.setQuestions(QuestionDtoMapper.fromDto(questions));
			feedbackRequestRepository.save(feedbackRequestDb);
		}
	}
}

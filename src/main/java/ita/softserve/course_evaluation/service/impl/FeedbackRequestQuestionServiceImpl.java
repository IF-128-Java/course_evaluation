package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestQuestionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		return questionDtoList.isEmpty() ? new ArrayList<>() : questionDtoList;
	}
	
	@Override
	public void assignQuestion(Long feedbackRequestId, List<Long> questionsId) {
		Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepository.findById(feedbackRequestId);
		List<Question> questions = questionRepository.findAllById(questionsId);
		List<Question> existQuestion = questionRepository.findAllQuestionsByFeedbackRequest(feedbackRequestId);
		questions.removeIf(existQuestion::contains);
		if (feedbackRequest.isPresent()) {
			FeedbackRequest feedbackRequestDb = feedbackRequest.get();
			List<Question> feedbackRequestQuestions = feedbackRequest.get().getQuestions();
			List<Long> questionIds = feedbackRequest.get().getQuestions()
					                         .stream()
					                         .map(Question::getId)
					                         .collect(Collectors.toList());
			if (!questionIds.contains(questionsId)) {
				feedbackRequestQuestions.addAll(feedbackRequestQuestions);
				feedbackRequestDb.setQuestions(feedbackRequestQuestions);
				feedbackRequestRepository.save(feedbackRequestDb);
			}
		}
	}
}

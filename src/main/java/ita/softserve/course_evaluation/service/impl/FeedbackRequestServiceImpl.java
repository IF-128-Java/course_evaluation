package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final QuestionRepository questionRepository;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository, QuestionRepository questionRepository) {
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.questionRepository = questionRepository;
	}
	
	@Override
	public FeedbackRequestDto create(FeedbackRequestDto dto) {
		List<Question> questions = getQuestions(dto);
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.save(FeedbackRequestDtoMapper.fromDto(dto, questions)));
	}
	
	private List<Question> getQuestions(FeedbackRequestDto dto) {
		List<Question> questions = questionRepository.findAllById(dto.getQuestionIds())
				                           .stream()
				                           .filter(question -> !question.isPattern())
				                           .collect(Collectors.toList());
		List<Question> isPattern = questionRepository.findAll()
				                           .stream()
				                           .filter(Question::isPattern)
				                           .collect(Collectors.toList());
		questions.addAll(isPattern);
		return questions;
	}
	
	@Override
	public FeedbackRequestDto update(FeedbackRequestDto dto) {
		List<Question> questions = getQuestions(dto);
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.save(FeedbackRequestDtoMapper.fromDto(dto, questions)));
	}
	
	@Override
	public void delete(Long id) {
		feedbackRequestRepository.deleteById(id);
	}
	
	@Override
	public FeedbackRequestDto getFeedbackRequestById(Long id) {
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.findById(id)
				                                   .orElseThrow(() -> new EntityNotFoundException("Feedback request with id " + id + " not found")));
	}
}

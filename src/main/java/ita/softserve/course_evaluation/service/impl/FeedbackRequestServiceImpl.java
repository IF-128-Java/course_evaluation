package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final QuestionRepository questionRepository;
	private final FeedbackRequestDtoMapper feedbackRequestDtoMapper;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository, QuestionRepository questionRepository, FeedbackRequestDtoMapper feedbackRequestDtoMapper) {
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.questionRepository = questionRepository;
		this.feedbackRequestDtoMapper = feedbackRequestDtoMapper;
	}
	
	@Override
	public FeedbackRequestDto create(FeedbackRequestDto dto) {
		
		FeedbackRequest feedbackRequest = feedbackRequestDtoMapper.fromDto(dto);
		FeedbackRequest feedbackRequestNew = feedbackRequestRepository.save(feedbackRequest);
		FeedbackRequestDto feedbackRequestDto = feedbackRequestDtoMapper.toDto(feedbackRequestNew);
		return feedbackRequestDto;
	}
	
	@Override
	public FeedbackRequestDto update(FeedbackRequestDto dto) {
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.save(feedbackRequestDtoMapper.fromDto(dto)));
	}
	
	@Override
	public void delete(Long id) {
		feedbackRequestRepository.delete(feedbackRequestDtoMapper.fromDto(getFeedbackRequestById(id)));
	}
	
	@Override
	public FeedbackRequestDto getFeedbackRequestById(Long id) {
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.findById(id)
				                                   .orElseThrow(() -> new EntityNotFoundException("Feedback request with id " + id + " not found")));
	}
}

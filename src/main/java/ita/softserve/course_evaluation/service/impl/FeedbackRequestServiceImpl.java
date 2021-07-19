package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.mapper.impl.FeedbackRequestMapper;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final FeedbackRequestMapper feedbackRequestMapper;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository, FeedbackRequestMapper feedbackRequestMapper) {
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.feedbackRequestMapper = feedbackRequestMapper;
	}
	
	@Override
	public void create(FeedbackRequestDto dto) {
		feedbackRequestRepository.save(feedbackRequestMapper.toEntity(dto));
	}
	
	@Override
	public void update(FeedbackRequestDto dto) {
		feedbackRequestRepository.save(feedbackRequestMapper.toEntity(dto));
	}
	
	@Override
	public void delete(Long id) {
		feedbackRequestRepository.delete(feedbackRequestMapper.toEntity(getFeedbackRequestById(id)));
	}
	
	@Override
	public FeedbackRequestDto getFeedbackRequestById(Long id) {
		return feedbackRequestMapper.toDto(feedbackRequestRepository.findById(id)
				       .orElseThrow(()-> new EntityNotFoundException("Feedback request with id " + id + " not found")));
	}
}

package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.mapper.impl.FeedbackMapper;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import ita.softserve.course_evaluation.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	private final FeedbackMapper feedbackMapper;
	
	public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper) {
		this.feedbackRepository = feedbackRepository;
		this.feedbackMapper = feedbackMapper;
	}
	
	@Override
	public FeedbackDto create(FeedbackDto dto) {
		return feedbackMapper.toDto(feedbackRepository.save(feedbackMapper.toEntity(dto)));
	}
	
	@Override
	public void delete(Long id) {
		feedbackRepository.delete(feedbackMapper.toEntity(getFeedbackById(id)));
	}
	
	@Override
	public FeedbackDto update(FeedbackDto dto) {
		return feedbackMapper.toDto(feedbackRepository.save(feedbackMapper.toEntity(dto)));
	}
	
	@Override
	public FeedbackDto getFeedbackById(Long id) {
		return feedbackMapper.toDto(feedbackRepository.findById(id)
				                            .orElseThrow(() -> new EntityNotFoundException("Feedback with id " + id + " not found")));
	}
}

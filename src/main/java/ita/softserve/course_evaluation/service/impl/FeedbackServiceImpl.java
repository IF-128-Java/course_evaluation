package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.FeedbackDtoMapper;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import ita.softserve.course_evaluation.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	
	public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}
	
	@Override
	public FeedbackDto create(FeedbackDto dto) {
		return FeedbackDtoMapper.toDto(feedbackRepository.save(FeedbackDtoMapper.fromDto(dto)));
	}
	
	@Override
	public void delete(Long id) {
		feedbackRepository.delete(FeedbackDtoMapper.fromDto(getFeedbackById(id)));
	}
	
	@Override
	public FeedbackDto update(FeedbackDto dto) {
		return FeedbackDtoMapper.toDto(feedbackRepository.save(FeedbackDtoMapper.fromDto(dto)));
	}
	
	@Override
	public FeedbackDto getFeedbackById(Long id) {
		return FeedbackDtoMapper.toDto(feedbackRepository.findById(id)
				                            .orElseThrow(() -> new EntityNotFoundException("Feedback with id " + id + " not found")));
	}
}

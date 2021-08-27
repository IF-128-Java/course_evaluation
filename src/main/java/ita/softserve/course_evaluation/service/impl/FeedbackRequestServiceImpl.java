package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository) {
		this.feedbackRequestRepository = feedbackRequestRepository;
	}
	
	@Override
	public FeedbackRequestDto create(FeedbackRequestDto dto) {
		FeedbackRequest feedbackRequest = FeedbackRequestDtoMapper.fromDto(dto);
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.save(feedbackRequest));
	}
	
	
	@Override
	public FeedbackRequestDto update(FeedbackRequestDto dto) {
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.save(FeedbackRequestDtoMapper.fromDto(dto)));
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
	
	@Override
	public Page<FeedbackRequestDto> findAllByCourseId(Pageable pageable, Long id) {
		 return  feedbackRequestRepository.findAllByCourseId(pageable, id).map(FeedbackRequestDtoMapper::toDto);
	}

	@Override
	public List<FeedbackRequestDto> getFeedbackRequestByCourseIdOnly(long id) {
		List<FeedbackRequestDto> feedbackRequestDto = FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.getFeedbackRequestByCourseIdOnly(id));
		return Objects.isNull(feedbackRequestDto) ? Collections.emptyList() : feedbackRequestDto;
	}

}

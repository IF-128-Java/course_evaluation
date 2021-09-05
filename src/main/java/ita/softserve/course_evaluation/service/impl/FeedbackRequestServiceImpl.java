package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
<<<<<<< HEAD
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDto;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
=======
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
>>>>>>> master
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final FeedbackRepository feedbackRepository;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository, FeedbackRepository feedbackRepository) {
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.feedbackRepository = feedbackRepository;
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

	@Override
	public List<StudentFeedbackRequestDto> getFeedbackRequestByCourseIdAndStudentId(long idc, long ids) {

		List<FeedbackRequest> feedbackRequests = feedbackRequestRepository.getFeedbackRequestByCourseIdOnly(idc);
		List<StudentFeedbackRequestDto> studentFeedbackRequestDtos = new ArrayList<>();

		if (!feedbackRequests.isEmpty()) {
			studentFeedbackRequestDtos = StudentFeedbackRequestDtoMapper.toDto(feedbackRequests);
		}

		for (StudentFeedbackRequestDto studentFeedbackRequestDto :studentFeedbackRequestDtos) {
			if (!feedbackRepository.getFeedbackByStudentId(studentFeedbackRequestDto.getId(), ids).isEmpty()) {
				studentFeedbackRequestDto.setStudentId(ids);
			}
		}

		return Objects.isNull(studentFeedbackRequestDtos) ? Collections.emptyList() : studentFeedbackRequestDtos;
	}


	@Override
	public List<FeedbackRequestDto> findAllByStatusActiveAndValidDate(long id) {
		List<FeedbackRequestDto> feedbackRequestDto = FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.findAllByStatusAndValidDate(id));
		return Objects.isNull(feedbackRequestDto) ? Collections.emptyList() : feedbackRequestDto;
	}
	
	@Override
	public void changeStatusAndLastNotification(FeedbackRequestDto dto, int status) {
		FeedbackRequest feedbackRequest = feedbackRequestRepository.getById(dto.getId());
		feedbackRequest.setStatus(FeedbackRequestStatus.values()[status]);
		feedbackRequest.setLastNotification(LocalDateTime.now());
		feedbackRequestRepository.save(feedbackRequest);
	}
}

package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDto;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import ita.softserve.course_evaluation.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final FeedbackService feedbackService;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository, FeedbackService feedbackService) {
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.feedbackService = feedbackService;
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
		FeedbackRequest feedbackRequest = feedbackRequestRepository.getById(id);
		feedbackRequest.setStatus(FeedbackRequestStatus.DELETED);
		feedbackRequestRepository.save(feedbackRequest);
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
	public List<StudentFeedbackRequestDto> getFeedbackRequestByCourseIdAndStudentId(long courseId, long studentId) {

		List<FeedbackRequest> feedbackRequests = feedbackRequestRepository.getFeedbackRequestByCourseIdOnly(courseId);
		List<StudentFeedbackRequestDto> studentFeedbackRequestDtos = new ArrayList<>();
		List<StudentFeedbackRequestDto> feedbackRequestDtosSelected = new ArrayList<>();
		LocalDateTime today = LocalDateTime.now();

		studentFeedbackRequestDtos = StudentFeedbackRequestDtoMapper.toDto(feedbackRequests);

		for (StudentFeedbackRequestDto studentFeedbackRequestDto : studentFeedbackRequestDtos) {
			if (!feedbackService.getFeedbackByRequestIdAndStudentId(studentFeedbackRequestDto.getId(), studentId).isEmpty()) {
				studentFeedbackRequestDto.setStudentId(studentId);
				studentFeedbackRequestDto.setFeedbackId(feedbackService
						.getFeedbackByRequestIdAndStudentId(studentFeedbackRequestDto.getId(), studentId).get(0).getId());
				feedbackRequestDtosSelected.add(studentFeedbackRequestDto);
			} else {
				if (studentFeedbackRequestDto.getEndDate().isAfter(today)) {
					feedbackRequestDtosSelected.add(studentFeedbackRequestDto);
				}
			}
		}
		return feedbackRequestDtosSelected;
	}


	@Override
	public List<FeedbackRequestDto> findAllByStatusAndValidDate(int status) {
		List<FeedbackRequestDto> feedbackRequestDto = FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.findAllByStatusAndValidDate(status));
		return Objects.isNull(feedbackRequestDto) ? Collections.emptyList() : feedbackRequestDto;
	}
	
	@Override
	public void changeStatusAndLastNotification(FeedbackRequestDto dto, int status) {
		FeedbackRequest feedbackRequest = feedbackRequestRepository.getById(dto.getId());
		feedbackRequest.setStatus(FeedbackRequestStatus.values()[status]);
		feedbackRequest.setLastNotification(LocalDateTime.now());
		feedbackRequestRepository.save(feedbackRequest);
	}
	
	@Override
	public void findAllByExpiredDateAndSetStatusToArchive(int status) {
		List<FeedbackRequest> feedbackRequest = feedbackRequestRepository.findAllByStatusAndExpireDate(status);
		feedbackRequest.forEach(fbr->{
			fbr.setStatus(FeedbackRequestStatus.ARCHIVE);
			feedbackRequestRepository.save(fbr);
		});
	}
	
	
}

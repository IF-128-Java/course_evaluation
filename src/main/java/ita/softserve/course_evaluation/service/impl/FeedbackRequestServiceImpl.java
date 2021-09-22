package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDto;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatMessageService;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import ita.softserve.course_evaluation.service.FeedbackService;
import ita.softserve.course_evaluation.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
	
	private static final int DEFAULT_FEEDBACK_REQUEST_DURATION = 7;
	private static final String DEFAULT_FEEDBACK_REQUEST_DESCRIPTION = "Share your impressions";
	
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final FeedbackService feedbackService;
	private final QuestionService questionService;
	private final ChatMessageService chatMessageService;
	
	public FeedbackRequestServiceImpl(FeedbackRequestRepository feedbackRequestRepository, FeedbackService feedbackService, QuestionService questionService, ChatMessageService chatMessageService) {
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.feedbackService = feedbackService;
		this.questionService = questionService;
		this.chatMessageService = chatMessageService;
	}
	
	@Override
	public FeedbackRequestDto create(FeedbackRequestDto dto, SecurityUser user) {
		FeedbackRequest feedbackRequest = FeedbackRequestDtoMapper.fromDto(dto);
		chatMessageService.processCreateMessageToGroupChatAboutNewFeedbackRequest(feedbackRequest, user);
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
		return feedbackRequestRepository.findAllByCourseId(pageable, id).map(FeedbackRequestDtoMapper::toDto);
	}
	
	@Override
	public List<FeedbackRequestDto> getFeedbackRequestByCourseIdOnly(long id) {
		return FeedbackRequestDtoMapper.toDto(feedbackRequestRepository.getFeedbackRequestByCourseIdOnly(id));
	}
	
	@Override
	public List<StudentFeedbackRequestDto> getFeedbackRequestByCourseIdAndStudentId(long courseId, long studentId) {
		
		List<FeedbackRequest> feedbackRequests = feedbackRequestRepository.getFeedbackRequestByCourseIdOnly(courseId);
		List<StudentFeedbackRequestDto> studentsFeedbackRequestDto = StudentFeedbackRequestDtoMapper.toDto(feedbackRequests);
		
		List<StudentFeedbackRequestDto> feedbackRequestsDtoSelected = new ArrayList<>();
		LocalDateTime today = LocalDateTime.now();
		
		for (StudentFeedbackRequestDto studentFeedbackRequestDto : studentsFeedbackRequestDto) {
			
			List<Feedback> feedbacks = feedbackService.getFeedbackByRequestIdAndStudentId(studentFeedbackRequestDto.getId(), studentId);
			if (!feedbacks.isEmpty()) {
				studentFeedbackRequestDto.setStudentId(studentId);
				studentFeedbackRequestDto.setFeedbackId(feedbacks.get(0).getId());
				feedbackRequestsDtoSelected.add(studentFeedbackRequestDto);
			} else {
				if (studentFeedbackRequestDto.getEndDate().isAfter(today)) {
					feedbackRequestsDtoSelected.add(studentFeedbackRequestDto);
				}
			}
		}
		return feedbackRequestsDtoSelected;
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
	
	@Override
	public void createDefaultFeedbackRequestByCourse(CourseDto course) {
		FeedbackRequest feedbackRequest = FeedbackRequest.builder()
				.course(CourseDtoMapper.toEntity(course))
				.feedbackDescription(DEFAULT_FEEDBACK_REQUEST_DESCRIPTION)
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(DEFAULT_FEEDBACK_REQUEST_DURATION))
				.status(FeedbackRequestStatus.ACTIVE)
				.questions(QuestionDtoMapper.fromDto(questionService.getAllQuestion()
						                                     .stream()
						                                     .filter(QuestionDto::isPattern)
						                                     .collect(Collectors.toList())))
		.build();
		feedbackRequestRepository.save(feedbackRequest);
		
	}
	
	
}

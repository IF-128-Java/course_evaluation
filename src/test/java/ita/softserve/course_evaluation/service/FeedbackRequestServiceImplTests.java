package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.impl.FeedbackRequestServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackRequestServiceImplTests {
	
	private static FeedbackRequest expected;
	private static FeedbackRequest feedbackRequest2;
	private static FeedbackRequestDto expectedDto;
	private static Pageable pageable;
	
	@Mock
	private FeedbackRequestRepository feedbackRequestRepository;
	@Mock
	private ChatMessageService chatMessageService;
	@InjectMocks
	private FeedbackRequestServiceImpl feedbackRequestService;
	
	@BeforeAll
	static void beforeAll() {
		User user = new User(1L, "Test", "Tester", "tester@test.com", "123");
		expected = new FeedbackRequest();
		expected.setId(1L);
		expected.setFeedbackDescription("FeedbackDescription");
		expected.setStartDate(LocalDateTime.now());
		expected.setEndDate(LocalDateTime.now().plusDays(5));
		expected.setStatus(FeedbackRequestStatus.DRAFT);
		expected.setQuestions(List.of(Question.builder().id(1L).build()));
		expected.setCourse(new Course(1L, "Desc", "Desc", new Date(), new Date(), user, null));
		
		feedbackRequest2 = new FeedbackRequest();
		feedbackRequest2.setId(1L);
		feedbackRequest2.setFeedbackDescription("FeedbackDescription");
		feedbackRequest2.setStartDate(LocalDateTime.now());
		feedbackRequest2.setEndDate(LocalDateTime.now().plusDays(5));
		feedbackRequest2.setStatus(FeedbackRequestStatus.ACTIVE);
		feedbackRequest2.setQuestions(List.of(Question.builder().id(1L).build()));
		feedbackRequest2.setCourse(new Course(1L, "Desc", "Desc", new Date(), new Date(), user, null));
		
		pageable = PageRequest.of(0, 10);
		expectedDto = FeedbackRequestDtoMapper.toDto(expected);
	}
	
	@Test
	void testAddFeedbackRequest() {
		SecurityUser securityUser = new SecurityUser(1L, "Nick", "password", Collections.emptyList(), true);

		when(feedbackRequestRepository.save(any())).thenReturn(expected);
		FeedbackRequestDto actual = feedbackRequestService.create(expectedDto, securityUser);
		assertEquals(expected.getId(), actual.getId());
		verify(feedbackRequestRepository, times(1)).save(any());
		verify(chatMessageService, times(1)).processCreateMessageToGroupChatAboutNewFeedbackRequest(any(), any());
	}
	
	@Test
	void testGetFeedbackRequest() {
		when(feedbackRequestRepository.findById(anyLong())).thenReturn(Optional.of(expected));
		FeedbackRequestDto actual = feedbackRequestService.getFeedbackRequestById(anyLong());
		assertEquals(expectedDto.getId(), actual.getId());
		verify(feedbackRequestRepository, times(1)).findById(anyLong());
	}
	
	@Test
	void testDeleteFeedbackRequest() {
		when(feedbackRequestRepository.getById(anyLong())).thenReturn(expected);
		feedbackRequestService.delete(anyLong());
		assertEquals(expected.getStatus(), FeedbackRequestStatus.DELETED);
	}
	
	@Test
	void testUpdateFeedbackRequest() {
		when(feedbackRequestRepository.save(any())).thenReturn(expected);
		FeedbackRequestDto actual = feedbackRequestService.update(expectedDto);
		assertEquals(expected.getId(), actual.getId());
		verify(feedbackRequestRepository, times(1)).save(any());
	}
	
	@Test
	void testChangeStatusAndLastNotification() {
		when(feedbackRequestRepository.getById(anyLong())).thenReturn(expected);
		feedbackRequestService.changeStatusAndLastNotification(expectedDto, 1);
		assertEquals(expected.getStatus(), FeedbackRequestStatus.ACTIVE);
		verify(feedbackRequestRepository, times(1)).getById(anyLong());
	}
	
	@Test
	void testFindAllByCourseId(){
		List<FeedbackRequest> feedbackRequests = List.of(expected);
		Page<FeedbackRequest> feedbackRequestPage = new PageImpl<>(feedbackRequests, pageable, feedbackRequests.size());
		when(feedbackRequestRepository.findAllByCourseId(pageable, 1L))
				.thenReturn(feedbackRequestPage);
		feedbackRequestService.findAllByCourseId(pageable,1L);
		verify(feedbackRequestRepository).findAllByCourseId(pageable,1L);
	}
}
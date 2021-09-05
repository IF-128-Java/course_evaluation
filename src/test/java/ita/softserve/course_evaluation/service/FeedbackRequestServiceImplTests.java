package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.service.impl.FeedbackRequestServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
	private static FeedbackRequestDto expectedDto;
	
	@Mock
	private FeedbackRequestRepository feedbackRequestRepository;
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
		
		expectedDto = FeedbackRequestDtoMapper.toDto(expected);
	}
	
	@Test
	void testAddFeedbackRequest() {
		when(feedbackRequestRepository.save(any())).thenReturn(expected);
		FeedbackRequestDto actual = feedbackRequestService.create(expectedDto);
		assertEquals(expected.getId(), actual.getId());
		verify(feedbackRequestRepository, times(1)).save(any());
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
}
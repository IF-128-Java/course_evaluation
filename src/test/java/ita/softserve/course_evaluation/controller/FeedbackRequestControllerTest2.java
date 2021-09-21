package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.exception.handler.GlobalControllerExceptionHandler;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {FeedbackRequestController.class, SpringSecurityTestConfiguration.class, GlobalControllerExceptionHandler.class})
class FeedbackRequestControllerTest2 {
	
	public static final String API_FEEDBACK_REQUEST_URL = "/api/v1/feedback_request";
	
	private ObjectMapper mapper;
	private FeedbackRequestDto feedbackRequestDto;
	
	@MockBean
	private FeedbackRequestService feedbackRequestService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void beforeEach() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		FeedbackRequest feedbackRequest = new FeedbackRequest();
		feedbackRequest.setId(1L);
		feedbackRequest.setStartDate(LocalDateTime.now().plusDays(1));
		feedbackRequest.setEndDate(LocalDateTime.now().plusDays(7));
		feedbackRequest.setStatus(FeedbackRequestStatus.DRAFT);
		feedbackRequest.setCourse(Course.builder().id(1L).build());
		feedbackRequest.setFeedbackDescription("Feedback description");
		feedbackRequest.setQuestions(Collections.emptyList());
		
		feedbackRequestDto = FeedbackRequestDtoMapper.toDto(feedbackRequest);
	}
	
	@Test
	@WithMockCustomUser(role = Role.ROLE_ADMIN)
	void testCreateFeedbackRequestWithAdmin() throws Exception {
		when(feedbackRequestService.create(any(FeedbackRequestDto.class), any(SecurityUser.class))).thenReturn(feedbackRequestDto);
		
		String actual = mockMvc.perform(post(API_FEEDBACK_REQUEST_URL)
				                                .contentType(MediaType.APPLICATION_JSON)
				                                .content(mapper.writeValueAsString(feedbackRequestDto))
				                                .accept(MediaType.APPLICATION_JSON))
				                .andExpect(jsonPath("$").exists())
				                .andExpect(jsonPath("$.id").value(feedbackRequestDto.getId()))
				                .andExpect(jsonPath("$.feedbackDescription").value(feedbackRequestDto.getFeedbackDescription()))
				                .andExpect(jsonPath("$.course").value(feedbackRequestDto.getCourse()))
				                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
		
		String expected = mapper.writeValueAsString(feedbackRequestDto);
		assertEquals(expected, actual);
		
		verify(feedbackRequestService, times(1)).create(any(FeedbackRequestDto.class), any(SecurityUser.class));
		verifyNoMoreInteractions(feedbackRequestService);
	}
	
	@Test
	@WithMockCustomUser
	void testCreateFeedbackRequestWithStudentRole() throws Exception {
		when(feedbackRequestService.create(any(FeedbackRequestDto.class), any(SecurityUser.class))).thenReturn(feedbackRequestDto);
		mockMvc.perform(post(API_FEEDBACK_REQUEST_URL)
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(mapper.writeValueAsString(feedbackRequestDto))
				                .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
		
		verify(feedbackRequestService, never()).create(any(FeedbackRequestDto.class), any(SecurityUser.class));
	}
	
	@Test
	@WithMockCustomUser
	void testGetExistFeedbackRequest() throws Exception {
		when(feedbackRequestService.getFeedbackRequestById(anyLong())).thenReturn(feedbackRequestDto);
		
		String actual = mockMvc.perform(get(API_FEEDBACK_REQUEST_URL + "/1")
				                                .contentType(MediaType.APPLICATION_JSON)
				                                .content(mapper.writeValueAsString(feedbackRequestDto))
				                                .accept(MediaType.APPLICATION_JSON))
				                .andExpect(jsonPath("$").exists())
				                .andExpect(jsonPath("$.id").value(feedbackRequestDto.getId()))
				                .andExpect(jsonPath("$.feedbackDescription").value(feedbackRequestDto.getFeedbackDescription()))
				                .andExpect(jsonPath("$.course").value(feedbackRequestDto.getCourse()))
				                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
		
		String expected = mapper.writeValueAsString(feedbackRequestDto);
		assertEquals(expected, actual);
		
		verify(feedbackRequestService, times(1)).getFeedbackRequestById(anyLong());
		verifyNoMoreInteractions(feedbackRequestService);
	}
	
	@Test
	@WithMockCustomUser(role = Role.ROLE_ADMIN)
	void testDeleteFeedbackRequestWithAdmin() throws Exception {
		mockMvc.perform(delete(API_FEEDBACK_REQUEST_URL + "/" + anyLong())
				                .contentType(MediaType.APPLICATION_JSON)
				                .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
		
		verify(feedbackRequestService, times(1)).delete(anyLong());
		verifyNoMoreInteractions(feedbackRequestService);
	}
	
	@Test
	@WithMockCustomUser
	void testDeleteFeedbackRequestWithStudentRole() throws Exception {
		mockMvc.perform(delete(API_FEEDBACK_REQUEST_URL + "/1")
				                .contentType(MediaType.APPLICATION_JSON)
				                .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()).andDo(print()).andReturn().getResponse().getContentAsString();
		
		verify(feedbackRequestService, never()).delete(anyLong());
	}
	
	@Test
	@WithMockCustomUser(role = Role.ROLE_ADMIN)
	void testEditFeedbackRequestWithAdminRole() throws Exception {
		mockMvc.perform(put(API_FEEDBACK_REQUEST_URL + "/")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(mapper.writeValueAsString(feedbackRequestDto))
				                .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
		
		verify(feedbackRequestService, times(1)).update(any(FeedbackRequestDto.class));
	}
	
	@Test
	@WithMockCustomUser
	void testEditFeedbackRequestWithStudentRole() throws Exception {
		mockMvc.perform(put(API_FEEDBACK_REQUEST_URL + "/")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(mapper.writeValueAsString(feedbackRequestDto))
				                .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()).andDo(print()).andReturn().getResponse().getContentAsString();
		
		verify(feedbackRequestService, never()).update(any(FeedbackRequestDto.class));
	}
}

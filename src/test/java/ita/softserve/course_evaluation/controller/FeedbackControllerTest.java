package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.AnswerDtoMapper;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.FeedbackDtoMapper;
import ita.softserve.course_evaluation.entity.*;
import ita.softserve.course_evaluation.exception.handler.GlobalControllerExceptionHandler;
import ita.softserve.course_evaluation.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mykhailo Fedenko on 01.09.2021
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {FeedbackController.class, SpringSecurityTestConfiguration.class, GlobalControllerExceptionHandler.class})
class FeedbackControllerTest {
    public static final String API_FEEDBACK_URL = "/api/v1/feedback";
    private ObjectMapper mapper;
    private FeedbackDto feedbackDto;

    @Autowired
    @Qualifier("withRoleUser")
    User withRoleUser;

    @MockBean
    private FeedbackService feedbackService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Feedback feedback1 = new Feedback();
        feedback1.setId(1L);
        feedback1.setComment("Test comment1");
        feedback1.setFeedbackRequest(new FeedbackRequest());
        feedback1.setStudent(new User());
        feedback1.setDate(LocalDateTime.now());

        AnswerToFeedback answer = new AnswerToFeedback();
        answer.setId(1L);
        answer.setFeedback(feedback1);
        answer.setQuestion(new Question());
        answer.setRate(5);

        feedbackDto = FeedbackDtoMapper.toDto(feedback1, AnswerDtoMapper.toDto(List.of(answer)));
    }


    @Test
    @WithMockCustomUser
    void testCreateFeedbackWithUserAuthorityRead() throws Exception {
        when(feedbackService.create(any(FeedbackDto.class))).thenReturn(feedbackDto);

        String actual = mockMvc.perform(post(API_FEEDBACK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(feedbackDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(feedbackDto.getId()))
                .andExpect(jsonPath("$.comment").value(feedbackDto.getComment()))
                .andExpect(jsonPath("$.answers[0].rate").value(feedbackDto.getAnswers().get(0).getRate()))
                .andExpect(jsonPath("$.answers[0].questionId").value(feedbackDto.getAnswers().get(0).getQuestionId()))
                .andExpect(jsonPath("$.answers[0].feedbackId").value(feedbackDto.getAnswers().get(0).getFeedbackId()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(feedbackDto);

        assertEquals(expected, actual);
        verify(feedbackService, times(1)).create(any(FeedbackDto.class));
        verifyNoMoreInteractions(feedbackService);

    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TEACHER)
    void testGetFeedback() throws Exception {
        when(feedbackService.getFeedbackById(Mockito.anyLong())).thenReturn(feedbackDto);

        String actual = mockMvc.perform(get(API_FEEDBACK_URL + "/1/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(feedbackDto.getId()))
                .andExpect(jsonPath("$.comment").value(feedbackDto.getComment()))
                .andExpect(jsonPath("$.answers[0].rate").value(feedbackDto.getAnswers().get(0).getRate()))
                .andExpect(jsonPath("$.answers[0].questionId").value(feedbackDto.getAnswers().get(0).getQuestionId()))
                .andExpect(jsonPath("$.answers[0].feedbackId").value(feedbackDto.getAnswers().get(0).getFeedbackId()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(feedbackDto), actual);
        verify(feedbackService, times(1)).getFeedbackById(Mockito.anyLong());
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_STUDENT)
    void testGetFeedbackWithNotPermitedRole() throws Exception {
        when(feedbackService.getFeedbackById(Mockito.anyLong())).thenReturn(feedbackDto);

        mockMvc.perform(get(API_FEEDBACK_URL + "/1/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof AccessDeniedException))
                .andExpect(jsonPath("status").value(403))
                .andExpect(jsonPath("message").value("Access is denied"))
                .andExpect(jsonPath("error").value("AccessDeniedException"))
                .andExpect(status().isForbidden()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(feedbackService, never()).getFeedbackById(Mockito.anyLong());
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TEACHER)
    void testGetAllFeedbackByFeedbackRequestId() throws Exception {
        Page<FeedbackDto> feedbackDtoPage = new PageImpl<>(List.of(feedbackDto));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "1");
        requestParams.add("size", "1");

        when(feedbackService.findAllByFeedbackRequestId(any(Pageable.class), Mockito.anyLong())).thenReturn(feedbackDtoPage);

        String actual = mockMvc.perform(get(API_FEEDBACK_URL + "/feedback_request/1/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content[0].id").value(feedbackDto.getId()))
                .andExpect(jsonPath("$.content[0].comment").value(feedbackDto.getComment()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(feedbackDtoPage), actual);
        verify(feedbackService, times(2)).findAllByFeedbackRequestId(Mockito.any(Pageable.class), Mockito.anyLong());
        verifyNoMoreInteractions(feedbackService);
    }
}
package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.AnswerDtoMapper;
import ita.softserve.course_evaluation.entity.AnswerToFeedback;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.exception.handler.GlobalControllerExceptionHandler;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

/**
 * @author Mykhailo Fedenko on 01.09.2021
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringSecurityTestConfiguration.class, AnswerToFeedbackController.class, GlobalControllerExceptionHandler.class})
class AnswerToFeedbackControllerTest {
    public static final String API_ANSWER_URL = "/api/v1/answers";

    private ObjectMapper mapper;
    private AnswerDto answerDto;

    @MockBean
    private AnswerToFeedbackService answerService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mapper = new ObjectMapper();

        Feedback feedback = new Feedback();
        feedback.setId(1L);

        AnswerToFeedback answer1 = new AnswerToFeedback();
        answer1.setId(1L);
        answer1.setRate(5);
        answer1.setFeedback(feedback);
        answer1.setQuestion(Question.builder().id(1L).build());

        answerDto = AnswerDtoMapper.toDto(answer1);
    }

    @Test
    @WithMockCustomUser
    void testGetAllAnswers() throws Exception {

        when(answerService.getAllAnswer()).thenReturn(List.of(answerDto));

        String actual = mockMvc.perform(get(API_ANSWER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(answerDto.getId()))
                .andExpect(jsonPath("$[0].rate").value(answerDto.getRate()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(List.of(answerDto));

        assertEquals(expected, actual);
        verify(answerService, times(1)).getAllAnswer();
        verifyNoMoreInteractions(answerService);
    }

    @Test
    @WithMockCustomUser
    void testGetAllAnswersWithNoUserExists() throws Exception {

        when(answerService.getAllAnswer()).thenReturn(null);

        mockMvc.perform(get(API_ANSWER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(answerService, times(1)).getAllAnswer();
        verifyNoMoreInteractions(answerService);
    }

    @Test
    void testAddAnswer() throws Exception {

        when(answerService.saveAnswer(any(AnswerDto.class))).thenReturn(answerDto);

        String actual = mockMvc.perform(post(API_ANSWER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(answerDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(answerDto.getId()))
                .andExpect(jsonPath("$.rate").value(answerDto.getRate()))
                .andExpect(jsonPath("$.feedbackId").value(answerDto.getFeedbackId()))
                .andExpect(jsonPath("$.questionId").value(answerDto.getQuestionId()))
                .andExpect(status().isCreated()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(answerDto);
        assertEquals(expected, actual);

        verify(answerService, times(1)).saveAnswer(Mockito.any(AnswerDto.class));
        verifyNoMoreInteractions(answerService);

    }

    @Test
    void testGetAnswerById() throws Exception {

        when(answerService.findAnswerById(Mockito.anyLong())).thenReturn(answerDto);

        String actual = mockMvc.perform(get(API_ANSWER_URL + "/1/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1")
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(answerDto.getId()))
                .andExpect(jsonPath("$.rate").value(answerDto.getRate()))
                .andExpect(jsonPath("$.feedbackId").value(answerDto.getFeedbackId()))
                .andExpect(jsonPath("$.questionId").value(answerDto.getQuestionId()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(answerDto);

        assertEquals(expected, actual);
        verify(answerService, times(1)).findAnswerById(Mockito.anyLong());
        verifyNoMoreInteractions(answerService);
    }

    @Test
    void testDeleteAnswer() throws Exception {

        doNothing().when(answerService).deleteAnswerById(Mockito.anyLong());

        mockMvc.perform(delete(API_ANSWER_URL+"/1/"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(answerService,times(1)).deleteAnswerById(Mockito.anyLong());
        verifyNoMoreInteractions(answerService);
    }

    @Test
    void testUpdateAnswer() throws Exception {
        when(answerService.updateAnswer(any(AnswerDto.class), Mockito.anyLong())).thenReturn(answerDto);

        String actual = mockMvc.perform(put(API_ANSWER_URL + "/1/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(answerDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(answerDto.getId()))
                .andExpect(jsonPath("$.rate").value(answerDto.getRate()))
                .andExpect(jsonPath("$.feedbackId").value(answerDto.getFeedbackId()))
                .andExpect(jsonPath("$.questionId").value(answerDto.getQuestionId()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(answerDto);

        assertEquals(expected, actual);
        verify(answerService, times(1)).updateAnswer(Mockito.any(AnswerDto.class), Mockito.anyLong());
        verifyNoMoreInteractions(answerService);
    }
}
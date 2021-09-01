package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.service.QuestionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mykhailo Fedenko on 31.08.2021
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {QuestionController.class, SpringSecurityTestConfiguration.class})
public class QuestionControllerTest {
    public static final String API_QUESTIONS_URL = "/api/v1/questions";

    private ObjectMapper mapper;
    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mapper = new ObjectMapper();

        question1 = Question.builder().questionText("Test question1").isPattern(true).id(1L).build();
        question2 = Question.builder().questionText("Test question2").isPattern(true).id(2L).build();
        question3 = Question.builder().questionText("Test question3").isPattern(false).id(3L).build();
        question4 = Question.builder().questionText("Test question4").isPattern(true).id(4L).build();
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(questionService);
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    public void testAddQuestion() throws Exception {

        when(questionService.saveQuestion(any(QuestionDto.class))).thenReturn(question1);

        String actual = mockMvc.perform(post(API_QUESTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                QuestionDtoMapper.toDto(question1)
                        ))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("id").value(question1.getId()))
                .andExpect(jsonPath("questionText").value(question1.getQuestionText()))
                .andExpect(jsonPath("pattern").value(question1.isPattern()))
                .andExpect(status().isCreated())
                .andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(question1);

        assertEquals(expected, actual);

        verify(questionService, times(1)).saveQuestion(any(QuestionDto.class));
    }

    @Test
    @WithMockCustomUser
    public void testGetAllQuestions() throws Exception {

        List<QuestionDto> questionDTOs = QuestionDtoMapper.toDto(List.of(question1, question2, question3, question4));
        QuestionDto questionDto2 = QuestionDtoMapper.toDto(question2);

        when(questionService.getAllQuestion()).thenReturn(questionDTOs);

        String actual = mockMvc.perform(get(API_QUESTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[1].id").value(questionDto2.getId()))
                .andExpect(jsonPath("$[1].pattern").value(questionDto2.isPattern()))
                .andExpect(jsonPath("$[1].questionText").value(questionDto2.getQuestionText()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(questionDTOs);
        assertEquals(expected, actual);
        verify(questionService, times(1)).getAllQuestion();
    }

    @Test
    @WithMockCustomUser
    public void testGetAllQuestionsWithEmptyQuestionTable() throws Exception {

        when(questionService.getAllQuestion()).thenReturn(null);

        mockMvc.perform(get(API_QUESTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(questionService, times(1)).getAllQuestion();
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    public void testGetQuestionById() throws Exception {

        QuestionDto questionDto = QuestionDtoMapper.toDto(question1);
        when(questionService.findQuestionById(Mockito.anyLong())).thenReturn(questionDto);

        String actual = mockMvc.perform(get(API_QUESTIONS_URL + "/1/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("id").value(questionDto.getId()))
                .andExpect(jsonPath("questionText").value(questionDto.getQuestionText()))
                .andExpect(jsonPath("pattern").value(questionDto.isPattern()))
                .andExpect(status().isOk())
                .andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(questionDto), actual);
        verify(questionService, times(1)).findQuestionById(Mockito.anyLong());
    }

    @Test
    public void testUpdateQuestion() throws Exception {
        QuestionDto questionDto = QuestionDtoMapper.toDto(question3);

        when(questionService.updateQuestion(any(QuestionDto.class), Mockito.anyLong())).thenReturn(questionDto);

        String actual = mockMvc.perform(put(API_QUESTIONS_URL + "/3/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("id").value(questionDto.getId()))
                .andExpect(jsonPath("$.pattern").value(questionDto.isPattern()))
                .andExpect(jsonPath("$.questionText").value(questionDto.getQuestionText()))
                .andExpect(status().isOk())
                .andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(questionDto);

        assertEquals(expected, actual);
        verify(questionService, times(1)).updateQuestion(any(QuestionDto.class), Mockito.anyLong());
    }

    @Test
    public void testDeleteQuestion() throws Exception {
        String expected = "Question deleted successfully!.";
        doNothing().when(questionService).deleteQuestionById(Mockito.anyLong());

        String actual = mockMvc.perform(delete(API_QUESTIONS_URL + "/4/")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").exists())
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(expected, actual);
        verify(questionService, times(1)).deleteQuestionById(Mockito.anyLong());


    }

}

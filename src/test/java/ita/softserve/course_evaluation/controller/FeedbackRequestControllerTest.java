package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDto;
import ita.softserve.course_evaluation.dto.StudentFeedbackRequestDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.FeedbackRequestService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Volodymyr Maliarchuk
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {FeedbackRequestController.class, SpringSecurityTestConfiguration.class})
public class FeedbackRequestControllerTest {
    public static final String API_FEEDBACKREQUEST_URL = "/api/v1/feedback_request";

    private ObjectMapper mapper;
    private Course course;
    private FeedbackRequest feedbackRequest;
    private User student;

    @MockBean
    private FeedbackRequestService feedbackRequestService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {

        mapper = new ObjectMapper();

        student = new User();
        student.setId(1L);
        student.setFirstName("Erik");
        student.setLastName("Sparks");
        student.setEmail("erik@testmail.com");
        student.setRoles(Set.of(Role.ROLE_STUDENT));
        student.setPassword("password");

        course = new Course();
        course.setId(1L);
        course.setCourseName("Java course");
        course.setDescription("Description");

        feedbackRequest = new FeedbackRequest();
        feedbackRequest.setId(1L);
        feedbackRequest.setFeedbackDescription("description");
        feedbackRequest.setCourse(course);
        feedbackRequest.setStatus(FeedbackRequestStatus.ACTIVE);

    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(feedbackRequestService);
    }

    @Test
    @WithMockCustomUser()
    void testGetFeedbackRequestByCourseIdOnly() throws Exception {

        List<FeedbackRequestDto> feedbackRequestDtos = FeedbackRequestDtoMapper.toDto(List.of(feedbackRequest));
        when(feedbackRequestService.getFeedbackRequestByCourseIdOnly(Mockito.anyLong())).thenReturn(feedbackRequestDtos);

        Long id = course.getId();
        String actual = mockMvc.perform(get(API_FEEDBACKREQUEST_URL + "/student/course/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(feedbackRequest.getId()))
                .andExpect(jsonPath("$[0].feedbackDescription").value("description"))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(feedbackRequestDtos);
        assertEquals(expected, actual);
        verify(feedbackRequestService, times(1)).getFeedbackRequestByCourseIdOnly(Mockito.anyLong());

    }

    @Test
    @WithMockCustomUser()
    void getFeedbackRequestByCourseIdAndStudentId() throws Exception {

        List<StudentFeedbackRequestDto> feedbackRequestDtos = StudentFeedbackRequestDtoMapper.toDto(List.of(feedbackRequest));
        when(feedbackRequestService.getFeedbackRequestByCourseIdAndStudentId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(feedbackRequestDtos);

        Long idc = course.getId();
        Long ids = student.getId();

        String actual = mockMvc.perform(get(API_FEEDBACKREQUEST_URL + "/course/{idc}/student/{ids}", idc, ids)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(feedbackRequest.getId()))
                .andExpect(jsonPath("$[0].feedbackDescription").value("description"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(feedbackRequestDtos);
        assertEquals(expected, actual);
        verify(feedbackRequestService, times(1)).getFeedbackRequestByCourseIdAndStudentId(Mockito.anyLong(), Mockito.anyLong());
    }
}

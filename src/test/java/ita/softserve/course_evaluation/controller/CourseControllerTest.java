package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.CourseService;
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

import java.util.Date;
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
@ContextConfiguration(classes = {CourseController.class, SpringSecurityTestConfiguration.class})
class CourseControllerTest {
    public static final String API_COURSES_URL = "/api/v1/courses/";

    private ObjectMapper mapper;

    private Course course1;

    private User teacher;

    private Group group;

    @MockBean
    private CourseService courseService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mapper = new ObjectMapper();

        group = new Group();
        group.setId(1L);
        group.setGroupName("Group_1");

        teacher = new User();
        teacher.setId(1L);
        teacher.setFirstName("Erik");
        teacher.setLastName("Sparks");
        teacher.setEmail("erik@testmail.com");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");

        course1 = new Course();
        course1.setId(1L);
        course1.setCourseName("Java course");
        course1.setDescription("Description");
        course1.setGroups(Set.of(group));

        course1.setTeacher(teacher);

    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(courseService);
    }

    @Test
    @WithMockCustomUser()
    void getFinishedCoursesByGroupId() throws Exception {

        Date startDate = new Date(new Date().getTime() - 864000000);
        Date endDate = new Date(new Date().getTime() - 432000000);
        course1.setStartDate(startDate);
        course1.setEndDate(endDate);

        List<CourseDto> courseDtos = CourseDtoMapper.toDto(List.of(course1));
        when(courseService.getFinishedCoursesByGroupId(Mockito.anyLong())).thenReturn(courseDtos);

        Long id = group.getId();
        String actual = mockMvc.perform(get(API_COURSES_URL+ "group/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(course1.getId()))
                .andExpect(jsonPath("$[0].courseName").value("Java course"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expected = mapper.writeValueAsString(courseDtos);
        assertEquals(expected, actual);
        verify(courseService, times(1)).getFinishedCoursesByGroupId(Mockito.anyLong());

    }

    @Test
    @WithMockCustomUser()
    void getCurrentCoursesByGroupId() throws Exception {

        Date startDate = new Date(new Date().getTime() - 864000000);
        Date endDate = new Date(new Date().getTime() + 864000000);
        course1.setStartDate(startDate);
        course1.setEndDate(endDate);

        List<CourseDto> courseDtos = CourseDtoMapper.toDto(List.of(course1));
        when(courseService.getCurrentCoursesByGroupId((Mockito.anyLong()))).thenReturn(courseDtos);

        Long id = group.getId();
        String actual = mockMvc.perform(get(API_COURSES_URL+ "current/group/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(course1.getId()))
                .andExpect(jsonPath("$[0].courseName").value("Java course"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expected = mapper.writeValueAsString(courseDtos);
        assertEquals(expected, actual);
        verify(courseService, times(1)).getCurrentCoursesByGroupId(Mockito.anyLong());

    }

    @Test
    @WithMockCustomUser()
    void getExpectedCourses() throws Exception {

        Date startDate = new Date(new Date().getTime() + 432000000);
        Date endDate = new Date(new Date().getTime() + 864000000);
        course1.setStartDate(startDate);
        course1.setEndDate(endDate);

        List<CourseDto> courseDtos = CourseDtoMapper.toDto(List.of(course1));
        when(courseService.getAvailableCourses()).thenReturn(courseDtos);

        String actual = mockMvc.perform(get(API_COURSES_URL+ "/available/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(course1.getId()))
                .andExpect(jsonPath("$[0].courseName").value("Java course"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expected = mapper.writeValueAsString(courseDtos);
        assertEquals(expected, actual);
        verify(courseService, times(1)).getAvailableCourses();

    }


}

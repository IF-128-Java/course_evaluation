package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.dto.TeacherToCourseDto;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.service.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mykhailo Fedenko
 */
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TeacherController.class, SpringSecurityTestConfiguration.class})
class TeacherControllerTest {
    public static final String API_TEACHERS_URI = "/api/v1/teachers";
    private ObjectMapper mapper;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mapper = new ObjectMapper();
    }

    @AfterEach
    void afterEach() {
        Mockito.verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testGetTeachers() throws Exception {
        TeacherToCourseDto teacher1 = TeacherToCourseDto.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Green")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();
        TeacherToCourseDto teacher2 = TeacherToCourseDto.builder()
                .id(2L)
                .firstName("Nick")
                .lastName("Grey")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();

        when(teacherService.getAllTeachers()).thenReturn(List.of(teacher1, teacher2));

        String actual = mockMvc.perform(get(API_TEACHERS_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].firstName").value("Mike"))
                .andExpect(jsonPath("$[1].firstName").value("Nick"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(List.of(teacher1, teacher2));

        assertEquals(expected, actual);
        verify(teacherService, times(2)).getAllTeachers();
    }

    @Test
    void testGetTeachersWhenNoTeacherExist() throws Exception {

        when(teacherService.getAllTeachers()).thenReturn(null);

        mockMvc.perform(get(API_TEACHERS_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(teacherService, times(1)).getAllTeachers();
    }

    @Test
    void testGetCourseById() throws Exception {
        TeacherToCourseDto teacher2 = TeacherToCourseDto.builder()
                .id(2L)
                .firstName("Nick")
                .lastName("Grey")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();
        when(teacherService.getTeacherById(Mockito.anyLong())).thenReturn(teacher2);

        String actual = mockMvc.perform(get(API_TEACHERS_URI + "/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.firstName").value("Nick"))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.lastName").value("Grey"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(teacher2), actual);
        verify(teacherService, times(2)).getTeacherById(Mockito.anyLong());

        //when teacher not found
        reset(teacherService);
        when(teacherService.getTeacherById(Mockito.anyLong())).thenReturn(null);
        mockMvc.perform(get(API_TEACHERS_URI+"/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(teacherService, times(1)).getTeacherById(Mockito.anyLong());
    }
}
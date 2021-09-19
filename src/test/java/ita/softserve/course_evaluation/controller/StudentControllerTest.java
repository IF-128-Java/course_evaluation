package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.dto.MailDto;
import ita.softserve.course_evaluation.dto.StudentDto;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.service.StudentService;
import ita.softserve.course_evaluation.service.mail.EmailService;
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

import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

/**
 * @author Mykhailo Fedenko
 */
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {StudentController.class, SpringSecurityTestConfiguration.class})
class StudentControllerTest {
    public static final String API_STUDENT_URL = "/api/v1/students";
    private ObjectMapper mapper;
    private StudentDto student1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;
    @MockBean
    private EmailService emailService;

    @BeforeEach
    void beforeEach() {
        mapper = new ObjectMapper();
        student1 = new StudentDto();
        student1.setId(1L);
        student1.setFirstName("Mike");
        student1.setLastName("Green");
        student1.setEmail("student@mail.com");
        student1.setRoles(Set.of(Role.ROLE_STUDENT));
        student1.setGroupId(1L);
        student1.setGroupName("IF-128");
        student1.setGroupChatRoomId(1L);
    }

    @AfterEach
    void afterEach(){
        Mockito.verifyNoMoreInteractions(studentService, emailService);
    }

    @Test
    void testGetStudentById() throws Exception {

        when(studentService.getById(Mockito.anyLong())).thenReturn(student1);

        String actual = mockMvc.perform(get(API_STUDENT_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.firstName").value("Mike"))
                .andExpect(jsonPath("$.email").value("student@mail.com"))
                .andExpect(jsonPath("$.groupName").value("IF-128"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(student1);

        assertEquals(expected, actual);
        verify(studentService, times(1)).getById(Mockito.anyLong());
    }

    @Test
    void testGetStudentsByGroupId() throws Exception {
        when(studentService.getStudentsByGroupId(Mockito.anyLong())).thenReturn(List.of(student1));

        String actual = mockMvc.perform(get(API_STUDENT_URL + "/group/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].firstName").value("Mike"))
                .andExpect(jsonPath("$[0].email").value("student@mail.com"))
                .andExpect(jsonPath("$[0].groupName").value("IF-128"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(List.of(student1));

        assertEquals(expected, actual);
        verify(studentService, times(1)).getStudentsByGroupId(Mockito.anyLong());
    }

    @Test
    void testGetStudentsByCourseId() throws Exception {
        when(studentService.getStudentsByCourseId(Mockito.anyLong())).thenReturn(List.of(student1));

        String actual = mockMvc.perform(get(API_STUDENT_URL + "/course/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].firstName").value("Mike"))
                .andExpect(jsonPath("$[0].email").value("student@mail.com"))
                .andExpect(jsonPath("$[0].groupName").value("IF-128"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(List.of(student1));

        assertEquals(expected, actual);
        verify(studentService, times(1)).getStudentsByCourseId(Mockito.anyLong());

        //when student not found
        reset(studentService);
        when(studentService.getStudentsByCourseId(Mockito.anyLong())).thenReturn(null);
        mockMvc.perform(get(API_STUDENT_URL + "/course/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        verify(studentService, times(1)).getStudentsByCourseId(Mockito.anyLong());
    }

    @Test
    void testSendMailToSelectedStudents() throws Exception {
        MailDto mailDto = new MailDto();
        mailDto.setMessage("Some test message!");
        mailDto.setSubject("Test subject");
        mailDto.setTo("test@mail.com");

        doNothing().when(emailService).sendSimpleEmail(Mockito.any(MailDto.class));
        String actual = mockMvc.perform(post(API_STUDENT_URL + "/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mailDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.message").value("Some test message!"))
                .andExpect(jsonPath("$.subject").value("Test subject"))
                .andExpect(jsonPath("$.to").value("test@mail.com"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(mailDto), actual);
        verify(emailService, times(1)).sendSimpleEmail(Mockito.any(MailDto.class));

    }

    @Test
    void testSendMailToSelectedStudentError() throws Exception {
        MailDto mailDto = new MailDto();
        mailDto.setMessage("Some test message!");
        mailDto.setSubject("Test subject");
        mailDto.setTo("test@mail.com");

        doThrow(new AddressException()).when(emailService).sendSimpleEmail(Mockito.any(MailDto.class));
        mockMvc.perform(post(API_STUDENT_URL+"/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mailDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.message").value("Some test message!"))
                .andExpect(jsonPath("$.subject").value("Test subject"))
                .andExpect(jsonPath("$.to").value("test@mail.com"))
                .andExpect(status().is5xxServerError()).andDo(print()).andReturn().getResponse().getContentAsString();

        verify(emailService, times(1)).sendSimpleEmail(Mockito.any(MailDto.class));
    }
}
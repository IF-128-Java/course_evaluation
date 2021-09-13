package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.StudentDto;
import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.StudentServiceImpl;
import ita.softserve.course_evaluation.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private User expectedStudent1;
    private User expectedStudent2;
    private Group group;
    private ChatRoom chatRoom;

    @BeforeEach
    public void beforeEach() {

        chatRoom = new ChatRoom();
        chatRoom.setChatType(ChatType.GROUP);

        group = new Group();
        group.setGroupName("Group name");
        group.setId(01L);
        group.setChatRoom(chatRoom);

        expectedStudent1 = new User();
        expectedStudent1.setId(1L);
        expectedStudent1.setFirstName("First Name");
        expectedStudent1.setLastName("Last Name");
        expectedStudent1.setEmail("email@mail.com");
        expectedStudent1.setPassword("password");
        expectedStudent1.setGroup(group);

        expectedStudent2 = new User();
        expectedStudent2.setId(2L);
        expectedStudent2.setFirstName("First Name");
        expectedStudent2.setLastName("Last Name");
        expectedStudent2.setEmail("email@mail.com");
        expectedStudent2.setPassword("password");
        expectedStudent2.setGroup(group);

    }

    @AfterEach
    public void afterEach(){
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testReadStudentsByGroupId(){
        when(userRepository.getStudentsByGroupId(anyLong())).thenReturn(List.of(expectedStudent1, expectedStudent2));

        List<StudentDto> actual = studentService.getStudentsByGroupId(anyLong());

        assertEquals(2, actual.size());
        verify(userRepository, times(1)).getStudentsByGroupId(anyLong());
    }



}
package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Volodymyr Maliarchuk
 */

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseRepositoryTest2 {

    private ChatRoom chatRoom;
    private User teacher;
    private Group group;
    private Course course;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;


    @BeforeEach
    void beforeEach() {
        chatRoom = new ChatRoom();
        chatRoom.setChatType(ChatType.GROUP);

        teacher = new User();
        teacher.setFirstName("Harry");
        teacher.setLastName("Sparks");
        teacher.setEmail("harry@testcom");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");

        group = new Group();
        group.setGroupName("if128");
        group.setChatRoom(chatRoom);

        chatRoomRepository.save(chatRoom);
        userRepository.save(teacher);
        groupRepository.save(group);

        course = new Course();
        course.setCourseName("java");
        course.setDescription("Description");
        course.setGroups(Set.of(group));
        course.setTeacher(teacher);

    }

    @Test
    void testFinishedCoursesOfGroupIfExist(){

        // start date 10 days before today
        Date startDate = new Date(new Date().getTime() - 864000000);
        // end date 5 days before today
        Date endDate = new Date(new Date().getTime() - 432000000);

        course.setStartDate(startDate);
        course.setEndDate(endDate);

        courseRepository.save(course);
        group.setCourses(List.of(course));

        List<Course> expected = List.of(course);
        List<Course> actual = courseRepository.finishedCoursesOfGroup(group.getId());

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }


    @Test
    void testCurrentCoursesOfGroupIfExist(){

        // start date 10 days before today
        Date startDate = new Date(new Date().getTime() - 864000000);
        // end date 5 after today
        Date endDate = new Date(new Date().getTime() + 432000000);

        course.setStartDate(startDate);
        course.setEndDate(endDate);

        courseRepository.save(course);
        group.setCourses(List.of(course));

        List<Course> expected = List.of(course);
        List<Course> actual = courseRepository.currentCoursesOfGroup(group.getId());

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void testExpectedCoursesIfExist(){

        // start date 5 days after today
        Date startDate = new Date(new Date().getTime() + 432000000);
        // end date 10 days after today
        Date endDate = new Date(new Date().getTime() + 864000000);

        course.setStartDate(startDate);
        course.setEndDate(endDate);

        courseRepository.save(course);
        group.setCourses(List.of(course));

        List<Course> expected = List.of(course);
        List<Course> actual = courseRepository.getAvailableCourses();

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }
}
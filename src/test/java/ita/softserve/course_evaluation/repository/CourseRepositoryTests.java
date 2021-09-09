package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CourseRepositoryTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void testFindByCourseNameIfExists(){
        User user = new User();

        Course course = new Course();
        course.setCourseName("Course Name");
        course.setDescription("Description");
        course.setStartDate(new Date());
        course.setEndDate(new Date());
        course.setTeacher(userRepository.save(user));

        courseRepository.save(course);
        List<Course> expected = List.of(course);
        List<Course> actual = courseRepository.findCourseByName(course.getCourseName());

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void testFindByCourseNameIfNotExist(){
        List<Course> actual = courseRepository.findCourseByName(StringUtils.EMPTY);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testFinishedCoursesOfGroupIfExist(){

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatType(ChatType.GROUP);

        User teacher = new User();
        teacher.setFirstName("Harry");
        teacher.setLastName("Sparks");
        teacher.setEmail("harry@testcom");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");

        Group group = new Group();
        group.setGroupName("if128");
        group.setChatRoom(chatRoom);

        Date startDate = new Date(new Date().getTime() - 864000000);
        Date endDate = new Date(new Date().getTime() - 432000000);

        Course course = new Course();
                course.setCourseName("java");
                course.setDescription("Description");
                course.setStartDate(startDate);
                course.setEndDate(endDate);
                course.setGroups(Set.of(group));
                course.setTeacher(teacher);

        chatRoomRepository.save(chatRoom);
        userRepository.save(teacher);
        groupRepository.save(group);
        courseRepository.save(course);
        group.setCourses(List.of(course));

        List<Course> expected = List.of(course);
        System.out.println(expected.size());

        List<Course> actual = courseRepository.finishedCoursesOfGroup(group.getId());
        System.out.println(actual.size());

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }


    @Test
    void testCurrentCoursesOfGroupIfExist(){

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatType(ChatType.GROUP);

        User teacher = new User();
        teacher.setFirstName("Harry");
        teacher.setLastName("Sparks");
        teacher.setEmail("harry@testcom");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");

        Group group = new Group();
        group.setGroupName("if128");
        group.setChatRoom(chatRoom);

        Date startDate = new Date(new Date().getTime() - 864000000);
        Date endDate = new Date(new Date().getTime() + 432000000);

        Course course = new Course();
        course.setCourseName("java");
        course.setDescription("Description");
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setGroups(Set.of(group));
        course.setTeacher(teacher);

        chatRoomRepository.save(chatRoom);
        userRepository.save(teacher);
        groupRepository.save(group);
        courseRepository.save(course);
        group.setCourses(List.of(course));

        List<Course> expected = List.of(course);
        System.out.println(expected.size());

        List<Course> actual = courseRepository.currentCoursesOfGroup(group.getId());
        System.out.println(actual.size());

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void testExpectedCoursesIfExist(){

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatType(ChatType.GROUP);

        User teacher = new User();
        teacher.setFirstName("Harry");
        teacher.setLastName("Sparks");
        teacher.setEmail("harry@testcom");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");

        Group group = new Group();
        group.setGroupName("if128");
        group.setChatRoom(chatRoom);

        Date startDate = new Date(new Date().getTime() + 432000000);
        Date endDate = new Date(new Date().getTime() + 864000000);

        Course course = new Course();
        course.setCourseName("java");
        course.setDescription("Description");
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setGroups(Set.of(group));
        course.setTeacher(teacher);

        chatRoomRepository.save(chatRoom);
        userRepository.save(teacher);
        groupRepository.save(group);
        courseRepository.save(course);
        group.setCourses(List.of(course));

        List<Course> expected = List.of(course);
        System.out.println(expected.size());

        List<Course> actual = courseRepository.getAvailableCourses();
        System.out.println(actual.size());

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }
}
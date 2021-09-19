package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Volodymyr Maliarchuk
 */

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FeedbackRepositoryTest {

    private User teacher;
    private User student1;
    private User student2;
    private User student3;

    private ChatRoom chatRoom;
    private Group group;
    private Course course;
    private FeedbackRequest feedbackRequestStatusSent;
    private Feedback feedBack1;
    private Feedback feedBack2;

    @Autowired
    private FeedbackRequestRepository feedbackRequestRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
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
        chatRoomRepository.save(chatRoom);

        group = new Group();
        group.setGroupName("Group Name");
        group.setChatRoom(chatRoom);
        groupRepository.save(group);

        student1 = new User();
        student1.setFirstName("Jim");
        student1.setLastName("Wilson");
        student1.setEmail("harry@democom");
        student1.setRoles(Set.of(Role.ROLE_STUDENT));
        student1.setPassword("password");
        student1.setGroup(group);
        userRepository.save(student1);

        student2 = new User();
        student2.setFirstName("Nick");
        student2.setLastName("Williams");
        student2.setEmail("nick@democom");
        student2.setRoles(Set.of(Role.ROLE_STUDENT));
        student2.setPassword("password");
        student2.setGroup(group);
        userRepository.save(student2);

        student3 = new User();
        student3.setFirstName("Tom");
        student3.setLastName("Johnson");
        student3.setEmail("tom@democom");
        student3.setRoles(Set.of(Role.ROLE_STUDENT));
        student3.setPassword("password");
        student3.setGroup(group);
        userRepository.save(student3);

        teacher = new User();
        teacher.setFirstName("Harry");
        teacher.setLastName("Sparks");
        teacher.setEmail("harry@democom");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");
        userRepository.save(teacher);

        course = Course.builder()
                .courseName("TestCourse")
                .description("Description")
                .startDate(new Date())
                .endDate(new Date())
                .groups(Set.of(group))
                .teacher(teacher)
                .build();
        courseRepository.save(course);

        feedbackRequestStatusSent = FeedbackRequest.builder()
                .feedbackDescription("description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .status(FeedbackRequestStatus.SENT)
                .course(course).build();
        feedbackRequestRepository.save(feedbackRequestStatusSent);

        feedBack1 = new Feedback();
        feedBack1.setDate(LocalDateTime.now());
        feedBack1.setStudent(student1);
        feedBack1.setFeedbackRequest(feedbackRequestStatusSent);
        feedbackRepository.save(feedBack1);

        feedBack2 = new Feedback();
        feedBack2.setDate(LocalDateTime.now());
        feedBack2.setStudent(student2);
        feedBack1.setFeedbackRequest(feedbackRequestStatusSent);
        feedbackRepository.save(feedBack2);

    }

    @Test
    @DisplayName("Find all feedback by request ID and student ID")
    void getFeedbackByRequestIdAndStudentId() {

        List<Feedback> feedbackActual = feedbackRepository
                .getFeedbackByRequestIdAndStudentId(feedbackRequestStatusSent.getId(), student1.getId());
        List<Feedback> feedbackExpected = List.of(feedBack1, feedBack2);

        assertFalse(feedbackActual.isEmpty());
        assertEquals(1, feedbackActual.size());
        assertNotEquals(feedbackExpected.size(), feedbackActual.size());
    }

    @Test
    @DisplayName("Find all feedback by request ID and student ID if feedback not exist ")
    void getFeedbackByRequestIdAndStudentIdIfFeedbackNotExist() {

        List<Feedback> feedbackActual = feedbackRepository
                .getFeedbackByRequestIdAndStudentId(feedbackRequestStatusSent.getId(), student3.getId());
        assertTrue(feedbackActual.isEmpty());
    }
}

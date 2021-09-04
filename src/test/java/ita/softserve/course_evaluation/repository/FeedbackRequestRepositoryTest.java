package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FeedbackRequestRepositoryTest {
    private FeedbackRequest feedbackRequestStatusActiveAndValidDate;
    private FeedbackRequest feedbackRequestStatusActiveAndInvalidDate;
    private FeedbackRequest feedbackRequestStatusDraft;
    private FeedbackRequest feedbackRequestStatusSent;
    private Course course;
    private Pageable pageable;

    @Autowired
    private FeedbackRequestRepository feedbackRequestRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach(){
        User teacher = new User();
        teacher.setFirstName("Mike");
        teacher.setLastName("Wood");
        teacher.setEmail("mike@com");
        teacher.setRoles(Set.of(Role.ROLE_TEACHER));
        teacher.setPassword("password");
        userRepository.save(teacher);

        course = Course.builder()
                .courseName("TestCourse")
                .description("Description")
                .startDate(new Date())
                .endDate(new Date())
                .groups(Collections.emptySet())
                .teacher(teacher)
                .build();
        courseRepository.save(course);

        feedbackRequestStatusDraft = FeedbackRequest.builder()
                .feedbackDescription("description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .status(FeedbackRequestStatus.DRAFT)
                .course(course).build();

        feedbackRequestStatusActiveAndValidDate = FeedbackRequest.builder()
                .feedbackDescription("description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .status(FeedbackRequestStatus.ACTIVE)
                .course(course).build();

        feedbackRequestStatusActiveAndInvalidDate = FeedbackRequest.builder()
                .feedbackDescription("description")
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().minusDays(5))
                .status(FeedbackRequestStatus.ACTIVE)
                .course(course).build();

        feedbackRequestStatusSent = FeedbackRequest.builder()
                .feedbackDescription("description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .status(FeedbackRequestStatus.SENT)
                .course(course).build();

        feedbackRequestRepository.save(feedbackRequestStatusActiveAndValidDate);
        feedbackRequestRepository.save(feedbackRequestStatusActiveAndInvalidDate);
        feedbackRequestRepository.save(feedbackRequestStatusDraft);
        feedbackRequestRepository.save(feedbackRequestStatusSent);
        pageable = PageRequest.of(0, 25);
    }

    @Test
    @DisplayName("Find all feedback request by valid valid date and active status")
    void testFindAllFeedbackRequestByActiveStatusAndValidDate(){
        List<FeedbackRequest> actual  = feedbackRequestRepository.findAllByStatusAndValidDate(FeedbackRequestStatus.ACTIVE.ordinal());
        List<FeedbackRequest> expected = List.of(feedbackRequestStatusActiveAndValidDate);
        assertEquals(1,actual.size());
        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    @DisplayName("Find all by course id")
    void testFindAllFeedbackRequestByCourseId(){
        Page<FeedbackRequest> feedbackRequestPage = feedbackRequestRepository.findAllByCourseId(pageable,1L);
        List<FeedbackRequest> feedbackRequestsActual = feedbackRequestPage.getContent();
        List<FeedbackRequest> feedbackRequestsExpected = List.of(feedbackRequestStatusActiveAndValidDate,feedbackRequestStatusActiveAndInvalidDate,feedbackRequestStatusDraft,feedbackRequestStatusSent);
        assertEquals(feedbackRequestsExpected.size(),feedbackRequestsActual.size());
    }
}

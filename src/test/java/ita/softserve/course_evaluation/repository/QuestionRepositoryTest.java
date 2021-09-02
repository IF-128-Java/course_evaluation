package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mykhailo Fedenko
 */
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class QuestionRepositoryTest {
    private Question question1;
    private Question question2;
    private Question question3;
    private Course course;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private FeedbackRequestRepository feedbackRequestRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

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
        question1 = questionRepository.save(Question.builder().questionText("Question1 text").isPattern(true).build());
        question2 = questionRepository.save(Question.builder().questionText("Question2 text").isPattern(false).build());
        question3 = questionRepository.save(Question.builder().questionText("Question3 text").isPattern(true).build());
    }

    @Test
    @DisplayName("Find list of all questions by feedback id")
    void testFindAllQuestionsByFeedbackRequest(){

        FeedbackRequest feedbackRequest = new FeedbackRequest(1L, "description",
                LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                10L, course, Collections.emptyList(), List.of(question1, question3));
        feedbackRequestRepository.save(feedbackRequest);

        List<Question> actual = questionRepository.findAllQuestionsByFeedbackRequest(feedbackRequest.getId());
        List<Question> expected = List.of(question1,question3);

        assertArrayEquals(expected.toArray(), actual.toArray());
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("Find all questions id list by feedback request id")
    void testFindAllQuestionIdsByFeedbackRequest(){

        FeedbackRequest feedbackRequest = new FeedbackRequest(1L, "description",
                LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                10L, course, Collections.emptyList(), List.of(question1, question2, question3));
        feedbackRequestRepository.save(feedbackRequest);
        System.out.println(questionRepository.findAll());
        System.out.println(feedbackRequest);

        List<Long> actual = questionRepository.findAllQuestionIdsByFeedbackRequest(feedbackRequest.getId());
        List<Long> expected = List.of(question1.getId(), question2.getId(), question3.getId());

        assertEquals(3, actual.size());
        assertArrayEquals(expected.toArray(),actual.toArray());
    }
}

package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByCourseNameIfExists(){
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
    public void testFindByCourseNameIfNotExist(){
        List<Course> actual = courseRepository.findCourseByName(StringUtils.EMPTY);

        assertTrue(actual.isEmpty());
    }
}
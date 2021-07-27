package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void testFindByCourseNameIfExists(){
        Course course = new Course();
        course.setCourseName("Course Name");
        course.setDescription("Description");
        course.setStartDate(new Date());
        course.setEndDate(new Date());
        course.setUser(new User());

        Course expected = courseRepository.save(course);
        Optional<Course> actual = courseRepository.findByCourseName(expected.getCourseName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindByCourseNameIfNotExist(){
        Optional<Course> actual = courseRepository.findByCourseName("");

        assertFalse(actual.isPresent());
    }
}
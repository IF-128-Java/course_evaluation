package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
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
        List<Course> actual = courseRepository.findByCourseName(expected.getCourseName());

        assertFalse(actual.isEmpty());
//        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindByCourseNameIfNotExist(){
        List<Course> actual = courseRepository.findByCourseName(StringUtils.EMPTY);

        assertTrue(actual.isEmpty());
    }
}
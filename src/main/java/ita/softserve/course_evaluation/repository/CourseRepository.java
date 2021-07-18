package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    Optional<Course> findByCourseName(String courseName);
}

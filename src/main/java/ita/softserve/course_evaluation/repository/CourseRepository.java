package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseName(String courseName);
}

package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name FROM course c " +
            "LEFT JOIN users u ON c.teacher_id = u.id", nativeQuery = true)
    List<Course> findAllCourses();

    @Override
    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name FROM course c " +
            "LEFT JOIN users u ON c.teacher_id = u.id WHERE c.id = :id", nativeQuery = true)
    Optional<Course> findById(Long id);

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name FROM course c " +
            "LEFT JOIN users u ON c.teacher_id = u.id WHERE course_name LIKE %:courseName%", nativeQuery = true)
    List<Course> findCourseByName(@Param("courseName") String courseName);
}

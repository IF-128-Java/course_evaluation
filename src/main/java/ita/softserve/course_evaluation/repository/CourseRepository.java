package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name FROM course c LEFT JOIN users u ON c.teacher_id = u.id", nativeQuery = true)
    List<Course> findAllCourses();

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name, ur.role_id FROM course c LEFT JOIN users u ON c.teacher_id = u.id LEFT JOIN user_roles ur on u.id = ur.user_id WHERE role_id = 1", nativeQuery = true)
    Course findCourseByIdWithTeachers();

    List<Course> findByCourseName(String courseName);
}

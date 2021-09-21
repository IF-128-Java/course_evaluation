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

    @Query(value = "SELECT c.id, c.course_name, c.description, c.start_date, c.end_date, c.teacher_id FROM course c INNER JOIN course_group g ON c.id = g.course_id WHERE g.group_id = ?1 AND c.end_date < NOW() ORDER BY start_date ASC", nativeQuery = true)
    List<Course> finishedCoursesOfGroup(long id);

    @Query(value = "SELECT c.id, c.course_name, c.description, c.start_date, c.end_date, c.teacher_id FROM course c INNER JOIN course_group g ON c.id = g.course_id WHERE g.group_id = ?1 AND c.start_date < NOW() AND c.end_date > NOW() ORDER BY start_date ASC", nativeQuery = true)
    List<Course> currentCoursesOfGroup(long id);

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name FROM course c " +
            "LEFT JOIN users u ON c.teacher_id = u.id WHERE course_name LIKE %:courseName%", nativeQuery = true)
    List<Course> findCourseByName(@Param("courseName") String courseName);

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id, first_name, last_name FROM course c " +
            "LEFT JOIN users u ON c.teacher_id = u.id WHERE c.start_date >NOW() ORDER BY start_date ASC", nativeQuery = true)
    List<Course> getAvailableCourses();

    @Query(value = "SELECT c.* FROM course c LEFT JOIN course_feedback_request cfr on c.id = cfr.course_id WHERE c.end_date > NOW() AND cfr.course_id IS NULL",nativeQuery = true)
    List<Course> getExpiredCoursesWithoutFeedbackRequest();
}

package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT * From course_feedback WHERE feedback_request_id = ?1", nativeQuery = true)
    Page<Feedback> findAllFeedbackByFeedbackRequestId (Pageable pageable, Long id);

    @Query(value = "SELECT * From course_feedback WHERE feedback_request_id = ?1 AND student_id = ?2", nativeQuery = true)
    List<Feedback> getFeedbackByRequestIdAndStudentId(long requestId, long studentId);

    @Query(value = "select student_id, count(id) from course_feedback group by student_id", nativeQuery = true)
    List<Object[]> countFeedbackByStudent();
}

package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT * From course_feedback where feedback_request_id = ?1", nativeQuery = true)
    List<Feedback> findAllFeedbackByFeedbackRequestId (Long Id);

}

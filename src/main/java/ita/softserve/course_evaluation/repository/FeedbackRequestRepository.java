package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.FeedbackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Long> {
	
	@Query(value = "SELECT * From course_feedback_request where course_id = ?1", nativeQuery = true)
	Page<FeedbackRequest> findAllByCourseId (Pageable page, Long id);
	
}

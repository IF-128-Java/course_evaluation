package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.FeedbackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Long> {
	
	@Query(value = "SELECT * From course_feedback_request where course_id = ?1", nativeQuery = true)
	Page<FeedbackRequest> findAllByCourseId (Pageable page, Long id);
	
	@Query(value = "SELECT * From course_feedback_request cfr where cfr.status = ?1 and (CURRENT_DATE >= cast(cfr.start_date as date) and CURRENT_DATE <= cast(cfr.end_date as date))", nativeQuery = true)
	List<FeedbackRequest> findAllByStatusAndValidDate(Long id);

	@Query(value = "SELECT * From course_feedback_request where course_id = ?1 ORDER BY start_date", nativeQuery = true)
	List<FeedbackRequest> getFeedbackRequestByCourseIdOnly (long id);
	
}

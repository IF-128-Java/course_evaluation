package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.FeedbackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Long> {
	
	@Query(value = "SELECT * FROM course_feedback_request WHERE course_id = ?1", nativeQuery = true)
	Page<FeedbackRequest> findAllByCourseId (Pageable page, Long id);
	
	@Query(value = "SELECT * FROM course_feedback_request cfr WHERE cfr.status = ?1 and (CURRENT_DATE >= CAST(cfr.start_date AS DATE) AND CURRENT_DATE <= CAST(cfr.end_date AS DATE))", nativeQuery = true)
	List<FeedbackRequest> findAllByStatusAndValidDate(int status);

	@Query(value = "SELECT * FROM course_feedback_request WHERE course_id = ?1 ORDER BY start_date", nativeQuery = true)
	List<FeedbackRequest> getFeedbackRequestByCourseIdOnly (long id);
	
	@Query(value = "SELECT * FROM course_feedback_request cfr WHERE cfr.status = ?1 and CURRENT_DATE > CAST(cfr.end_date AS DATE)", nativeQuery = true)
	List<FeedbackRequest> findAllByStatusAndExpireDate(int status);
}

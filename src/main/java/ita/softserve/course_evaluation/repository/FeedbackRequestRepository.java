package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Long> {
}

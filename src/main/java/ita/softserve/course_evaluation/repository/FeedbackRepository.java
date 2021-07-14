package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}

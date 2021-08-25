package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.AnswerToFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerToFeedbackRepository extends JpaRepository<AnswerToFeedback, Long> {

}

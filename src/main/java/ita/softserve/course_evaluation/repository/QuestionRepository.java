package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
	
	@Query(value = "SELECT q.id as id, q.question_text as question_text, q.is_pattern as is_pattern FROM question q inner join course_feedback_request_question cfrq on q.id = cfrq.question_id where feedback_request_id = ?1", nativeQuery = true)
	List<Question> findAllQuestionsByFeedbackRequest(Long id);
	
	@Query(value = "SELECT question_id FROM course_feedback_request_question f where feedback_request_id = ?1", nativeQuery = true)
	List<Long> findAllQuestionIdsByFeedbackRequest(Long id);

}

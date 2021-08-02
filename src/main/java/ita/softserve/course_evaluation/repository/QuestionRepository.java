package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
	
	@Query("SELECT questions FROM FeedbackRequest f LEFT JOIN f.questions questions where f.id= :id")
	List<Question> findAllQuestionsByFeedbackRequest(Long id);
	
	@Query("SELECT questions.id FROM FeedbackRequest f LEFT JOIN f.questions questions where f.id= :id")
	List<Long> findAllQuestionIdsByFeedbackRequest(Long id);

}

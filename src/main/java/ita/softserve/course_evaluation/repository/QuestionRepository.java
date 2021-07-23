package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository <Question, Long> {
}

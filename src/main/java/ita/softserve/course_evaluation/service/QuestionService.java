package ita.softserve.course_evaluation.service;


import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.entity.Question;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestion();
    Question saveQuestion(QuestionDto question);
    QuestionDto findQuestionById(long id);
    void deleteQuestionById(long id);
    QuestionDto updateEmployee(QuestionDto question, long id);
}

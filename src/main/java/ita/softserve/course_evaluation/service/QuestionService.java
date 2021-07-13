package ita.softserve.course_evaluation.service;


import ita.softserve.course_evaluation.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestion();
    Question addQuestion(Question question);
    Question getQuestionById(long id);
    void deleteQuestionById(long id);
    Question updateEmployee(Question question, long id);
}

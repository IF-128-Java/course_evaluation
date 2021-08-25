package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.AnswerDto;

import java.util.List;

public interface AnswerToFeedbackService {

    List<AnswerDto> getAllAnswer();
    
    List<AnswerDto> getAllAnswerByFeedbackId(Long id);

    AnswerDto saveAnswer(AnswerDto answer);
    
    List<AnswerDto> saveAnswers(List<AnswerDto> answer);

    AnswerDto findAnswerById(long id);

    void deleteAnswerById(long id);

    AnswerDto updateAnswer(AnswerDto answer, long id);

}

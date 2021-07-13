package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Question> getAllQuestion() {
        return questionRepository.findAll();
    }

    @Override
    public Question addQuestion(Question question) {
        questionRepository.save(question);
        return question;
    }

    @Override
    public Question getQuestionById(long id) {
        Optional<Question> optional = questionRepository.findById(id);
        Question question;

        if(optional.isPresent()) {
            question = optional.get();
        } else {
            throw new RuntimeException("Question was not found for id: " + id);
        }
        return question;
//        return questionRepository.findById(id).orElseThrow(() ->
//                new RuntimeException("Question was not found for id: " + id));
    }

    @Override
    public void deleteQuestionById(long id) {
        questionRepository.findById(id)
                          .orElseThrow(() -> new RuntimeException("Question was not found for id: " + id));
        questionRepository.deleteById(id);
    }

    @Override
    public Question updateEmployee(Question question, long id) {
        Question existingQuestion = questionRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Question not found for id: " + id));
        existingQuestion.setQuestionText(question.getQuestionText());
        existingQuestion.setPattern(question.getPattern());
        existingQuestion.setFeedbackRequestId(question.getFeedbackRequestId());
        questionRepository.save(existingQuestion);
        return existingQuestion;
    }
}

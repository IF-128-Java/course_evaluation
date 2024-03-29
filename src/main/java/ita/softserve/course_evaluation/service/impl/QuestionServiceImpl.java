package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    public List<QuestionDto> getAllQuestion() {
        List<QuestionDto> questionDtoList = QuestionDtoMapper.toDto(questionRepository.findAll());
        return questionDtoList.isEmpty() ? new ArrayList<>() : questionDtoList;
    }

    @Override
    public Question saveQuestion(QuestionDto questionDto) {
        return questionRepository.save(QuestionDtoMapper.fromDto(questionDto));
    }

    @Override
    public QuestionDto findQuestionById(long id) {
        return QuestionDtoMapper.toDto(questionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Question was not found for id: " + id)));
    }

    @Override
    public void deleteQuestionById(long id) {
        questionRepository.delete(QuestionDtoMapper.fromDto(findQuestionById(id)));
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto, long id) {
        QuestionDto existingQuestionDto = findQuestionById(id);
        existingQuestionDto.setQuestionText(questionDto.getQuestionText());
        existingQuestionDto.setPattern(questionDto.isPattern());
        questionRepository.save(QuestionDtoMapper.fromDto(existingQuestionDto));
        return existingQuestionDto;
    }
}

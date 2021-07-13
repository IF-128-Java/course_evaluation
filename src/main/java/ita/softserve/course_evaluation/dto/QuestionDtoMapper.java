package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Question;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionDtoMapper {

    public static Question fromDto(QuestionDto questionDto) {
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setQuestionText(questionDto.getQuestionText());
        question.setPattern(questionDto.getPattern());
        question.setFeedbackRequestId(questionDto.getFeedbackRequestId());
        return question;
    }

    public static List<Question> fromDto(List<QuestionDto> questionDto) {
        return questionDto.stream()
                .map(QuestionDtoMapper::fromDto)
                .collect(Collectors.toList());
    }

    public static QuestionDto toDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setPattern(question.getPattern());
        dto.setFeedbackRequestId(question.getFeedbackRequestId());
        return dto;

    }

    public static List<QuestionDto> toDto(List<Question> question) {
        return question.stream()
                .map(QuestionDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}

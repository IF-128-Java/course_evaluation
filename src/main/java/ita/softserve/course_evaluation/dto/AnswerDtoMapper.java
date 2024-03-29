package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.AnswerToFeedback;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.Question;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnswerDtoMapper {

    public static AnswerToFeedback fromDto(AnswerDto dto) {
        AnswerToFeedback answer = null;
        if (dto != null) {
            Question question = new Question();
            Feedback feedback = new Feedback();
            feedback.setId(dto.getFeedbackId());
            question.setId(dto.getQuestionId());
            answer = new AnswerToFeedback(dto.getId(), dto.getRate(), question, feedback);
        }
        return answer;
    }

    public static List<AnswerToFeedback> fromDto(List<AnswerDto> answerDtoList) {
        return Objects.isNull(answerDtoList)
                ? null
                : answerDtoList.stream()
                .map(AnswerDtoMapper::fromDto)
                .collect(Collectors.toList());
    }

    public static AnswerDto toDto(AnswerToFeedback answer) {
        AnswerDto dto = null;
        if (answer != null) {
            dto = new AnswerDto(answer.getId(), answer.getRate(), answer.getQuestion().getId(), answer.getFeedback().getId());
        }
        return dto;
    }

    public static List<AnswerDto> toDto(List<AnswerToFeedback> answerList) {
        return Objects.isNull(answerList)
                ? null
                : answerList.stream()
                .map(AnswerDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}

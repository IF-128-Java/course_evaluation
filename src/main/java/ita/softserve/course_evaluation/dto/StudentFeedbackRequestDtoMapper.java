package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.service.FeedbackService;
import ita.softserve.course_evaluation.service.impl.FeedbackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StudentFeedbackRequestDtoMapper {

    public static StudentFeedbackRequestDto toDto(FeedbackRequest feedbackRequest) {

        return new StudentFeedbackRequestDto(feedbackRequest.getId(),
                                             feedbackRequest.getFeedbackDescription(),
                                             feedbackRequest.getStartDate(),
                                             feedbackRequest.getEndDate(),
                                             feedbackRequest.getCourse().getId(),
                                             null);
    }

    public static List<StudentFeedbackRequestDto> toDto(List<FeedbackRequest> feedbackRequests){

        return Objects.isNull(feedbackRequests)
                ? null
                : feedbackRequests.stream()
                .map(StudentFeedbackRequestDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}

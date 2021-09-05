package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.FeedbackRequest;

import java.util.Collections;
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
                                             null, null);
    }

    public static List<StudentFeedbackRequestDto> toDto(List<FeedbackRequest> feedbackRequests){

        return Objects.isNull(feedbackRequests)
                ? Collections.emptyList()
                : feedbackRequests.stream()
                .map(StudentFeedbackRequestDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}

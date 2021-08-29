package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.FeedbackRequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FeedbackRequestDtoMapper {
	
	public static FeedbackRequestDto toDto(FeedbackRequest feedbackRequest) {
		return new FeedbackRequestDto(feedbackRequest.getId(), feedbackRequest.getFeedbackDescription(), feedbackRequest.getStartDate(), feedbackRequest.getEndDate(), feedbackRequest.getCourse().getId(), feedbackRequest.getStatus().ordinal());
	}
	
	public static FeedbackRequest fromDto(FeedbackRequestDto dto) {
		FeedbackRequest feedbackRequest = null;
		if (dto != null) {
			Course course = new Course();
			course.setId(dto.getCourse());
			feedbackRequest = new FeedbackRequest(dto.getId(), dto.getFeedbackDescription(), dto.getStartDate(), dto.getEndDate(), 0L, course, new ArrayList<>(), new ArrayList<>(), FeedbackRequestStatus.values()[dto.getStatus()]);
		}
		return feedbackRequest;
	}
	
	public static List<FeedbackRequest> fromDto(List<FeedbackRequestDto> dto){
		return Objects.isNull(dto)
				? null
				: dto.stream()
						  .map(FeedbackRequestDtoMapper::fromDto)
						  .collect(Collectors.toList());
	}
	
	public static List<FeedbackRequestDto> toDto(List<FeedbackRequest> feedbackRequests){
		return Objects.isNull(feedbackRequests)
				       ? null
				       : feedbackRequests.stream()
						         .map(FeedbackRequestDtoMapper::toDto)
						         .collect(Collectors.toList());
	}
}

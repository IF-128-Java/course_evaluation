package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;

import java.util.ArrayList;

public class FeedbackRequestDtoMapper {
	
	public static FeedbackRequestDto toDto(FeedbackRequest feedbackRequest) {
		return new FeedbackRequestDto(feedbackRequest.getId(), feedbackRequest.getFeedbackDescription(), feedbackRequest.getStartDate(), feedbackRequest.getEndDate(), feedbackRequest.getCourse().getId());
	}
	
	public static FeedbackRequest fromDto(FeedbackRequestDto dto) {
		FeedbackRequest feedbackRequest = null;
		if (dto != null) {
			Course course = new Course();
			course.setId(dto.getCourse());
			feedbackRequest = new FeedbackRequest(dto.getId(), dto.getFeedbackDescription(), dto.getStartDate(), dto.getEndDate(), 0L, course, new ArrayList<>(), new ArrayList<>());
		}
		return feedbackRequest;
	}
}

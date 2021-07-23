package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackDtoMapper {
	
	public static Feedback fromDto(FeedbackDto dto) {
		Feedback feedback = null;
		if (dto != null) {
			FeedbackRequest feedbackRequest = new FeedbackRequest();
			User user = new User();
			feedbackRequest.setId(dto.getFeedbackRequestId());
			user.setId(dto.getStudentId());
			feedback = new Feedback(dto.getId(), dto.getDate(), dto.getComment(), user, feedbackRequest);
		}
		return feedback;
	}
	
	public static List<Feedback> fromDto(List<FeedbackDto> dto) {
		return dto == null ? null : dto.stream()
				                            .map(FeedbackDtoMapper::fromDto)
				                            .collect(Collectors.toList());
	}
	
	public static FeedbackDto toDto(Feedback feedback) {
		return feedback == null ? null : new FeedbackDto(feedback.getId(), feedback.getDate(), feedback.getComment(), feedback.getStudent().getId(), feedback.getFeedbackRequest().getId(), new ArrayList<>());
	}
	
	public static List<FeedbackDto> toDto(List<Feedback> feedbacks) {
		return feedbacks == null ? null : feedbacks.stream()
				                                  .map(FeedbackDtoMapper::toDto)
				                                  .collect(Collectors.toList());
	}
}

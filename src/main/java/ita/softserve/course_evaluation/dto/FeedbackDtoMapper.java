package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.User;

import java.util.List;

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
	
	public static FeedbackDto toDto(Feedback feedback, List<AnswerDto> answers) {
		return new FeedbackDto(feedback.getId(), feedback.getDate(), feedback.getComment(), feedback.getStudent().getId(), feedback.getFeedbackRequest().getId(), answers);
	}
	
}

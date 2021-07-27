package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackDtoMapper {
	
	private final AnswerToFeedbackService answerToFeedbackService;
	
	public FeedbackDtoMapper(AnswerToFeedbackService answerToFeedbackService) {
		this.answerToFeedbackService = answerToFeedbackService;
	}
	
	public Feedback fromDto(FeedbackDto dto) {
		Feedback feedback = null;
		if (dto != null) {
			FeedbackRequest feedbackRequest = new FeedbackRequest();
			User user = new User();
			feedbackRequest.setId(dto.getFeedbackRequestId());
			user.setId(dto.getStudentId());
			List<AnswerDto> answers = dto.getAnswers();
			answerToFeedbackService.saveAnswers(answers);
			feedback = new Feedback(dto.getId(), dto.getDate(), dto.getComment(), user, feedbackRequest);
		}
		return feedback;
	}
	
	public List<Feedback> fromDto(List<FeedbackDto> dto) {
		return dto == null ? null : dto.stream()
				                            .map(this::fromDto)
				                            .collect(Collectors.toList());
	}
	
	public FeedbackDto toDto(Feedback feedback) {
		
		List<AnswerDto> answers = answerToFeedbackService.getAllAnswerByFeedbackId(feedback.getId());
		return new FeedbackDto(feedback.getId(), feedback.getDate(), feedback.getComment(), feedback.getStudent().getId(), feedback.getFeedbackRequest().getId(), answers);
	}
	
	public List<FeedbackDto> toDto(List<Feedback> feedbacks) {
		return feedbacks == null ? null : feedbacks.stream()
				                                  .map(this::toDto)
				                                  .collect(Collectors.toList());
	}
}

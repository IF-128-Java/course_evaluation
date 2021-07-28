package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackRequestDtoMapper {
	
	public static FeedbackRequestDto toDto(FeedbackRequest feedbackRequest) {
		List<Long> questionIds = feedbackRequest.getQuestions().stream().map(Question::getId)
				                         .collect(Collectors.toList());
		return new FeedbackRequestDto(feedbackRequest.getId(), feedbackRequest.getFeedbackDescription(), feedbackRequest.getStartDate(), feedbackRequest.getEndDate(), feedbackRequest.getDuration(), feedbackRequest.getCourse().getId(), questionIds);
	}
	
	public static FeedbackRequest fromDto(FeedbackRequestDto dto, List<Question> questions) {
		FeedbackRequest feedbackRequest = null;
		if (dto != null) {
			Course course = new Course();
			course.setId(dto.getCourse());
			feedbackRequest = new FeedbackRequest(dto.getId(), dto.getFeedbackDescription(), dto.getStartDate(), dto.getEndDate(), dto.getDuration(), course, new ArrayList<>(), questions);
		}
		return feedbackRequest;
	}
}

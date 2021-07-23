package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackRequestDtoMapper {
	
	private final QuestionRepository questionRepository;
	
	public FeedbackRequestDtoMapper(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	public static FeedbackRequestDto toDto(FeedbackRequest feedbackRequest) {
		List<Long> questionIds = feedbackRequest.getQuestions().stream().map((question) -> question.getId())
				                         .collect(Collectors.toList());
		return new FeedbackRequestDto(feedbackRequest.getId(), feedbackRequest.getFeedbackDescription(), feedbackRequest.getStartDate(), feedbackRequest.getEndDate(), feedbackRequest.getDuration(), feedbackRequest.getCourse().getId(), questionIds);
	}
	
	public static List<FeedbackRequestDto> toDto(List<FeedbackRequest> feedbackRequest) {
		return feedbackRequest == null ? null : feedbackRequest.stream()
				                                        .map(FeedbackRequestDtoMapper::toDto)
				                                        .collect(Collectors.toList());
	}
	
	public List<FeedbackRequest> fromDtoList(List<FeedbackRequestDto> dto) {
		return dto == null ? null : dto.stream()
				                            .map(this::fromDto)
				                            .collect(Collectors.toList());
	}
	
	public FeedbackRequest fromDto(FeedbackRequestDto dto) {
		FeedbackRequest feedbackRequest = null;
		if (dto != null) {
			List<Question> list = questionRepository.findAllById(dto.getQuestionIds());
			Course course = new Course();
			course.setId(dto.getId());
			feedbackRequest = new FeedbackRequest(dto.getId(), dto.getFeedbackDescription(), dto.getStartDate(), dto.getEndDate(), dto.getDuration(), course, list);
		}
		return feedbackRequest;
	}
}

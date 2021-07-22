package ita.softserve.course_evaluation.mapper.impl;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.mapper.AbstractMapper;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import ita.softserve.course_evaluation.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

@Component
public class FeedbackMapper extends AbstractMapper<Feedback, FeedbackDto> {
	
	private final ModelMapper mapper;
	private final FeedbackRequestRepository feedbackRequestRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public FeedbackMapper(ModelMapper mapper, FeedbackRequestRepository feedbackRequestRepository, UserRepository userRepository) {
		super(Feedback.class, FeedbackDto.class);
		this.mapper = mapper;
		this.feedbackRequestRepository = feedbackRequestRepository;
		this.userRepository = userRepository;
	}
	
	@PostConstruct
	public void setupMapper() {
		mapper.createTypeMap(Feedback.class, FeedbackDto.class)
				.addMappings(mapping -> mapping.skip(FeedbackDto::setFeedbackRequestId))
				.addMappings(mapping -> mapping.skip(FeedbackDto::setStudentId))
				.setPostConverter(toDtoConverter());
		mapper.createTypeMap(FeedbackDto.class, Feedback.class)
				.addMappings(mapping -> mapping.skip(Feedback::setFeedbackRequest))
				.addMappings(mapping -> mapping.skip(Feedback::setStudent))
				.setPostConverter(toEntityConverter());
	}
	
	@Override
	protected void mapSpecificFieldsInEntity(Feedback source, FeedbackDto destination) {
		destination.setFeedbackRequestId(source.getFeedbackRequest().getId());
		destination.setStudentId(source.getStudent().getId());
	}
	
	@Override
	protected void mapSpecificFieldsInDto(FeedbackDto source, Feedback destination) {
		destination.setFeedbackRequest(getFeedbackRequest(source));
		destination.setStudent(getStudent(source));
	}
	
	private User getStudent(FeedbackDto source) {
		return userRepository.findById(source.getId())
				       .orElseThrow(() -> new EntityNotFoundException("User not found."));
	}
	
	private FeedbackRequest getFeedbackRequest(FeedbackDto source) {
		return feedbackRequestRepository.findById(source.getId())
				       .orElseThrow(() -> new EntityNotFoundException("FeedbackRequest not found."));
	}
}

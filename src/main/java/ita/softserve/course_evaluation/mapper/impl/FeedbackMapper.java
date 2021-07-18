package ita.softserve.course_evaluation.mapper.impl;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.mapper.AbstractMapper;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper extends AbstractMapper<Feedback, FeedbackDto> {
	
	private final ModelMapper mapper;
	private final FeedbackRepository feedbackRepository;
	
	@Autowired
	public FeedbackMapper(ModelMapper mapper, FeedbackRepository feedbackRepository) {
		super(Feedback.class, FeedbackDto.class);
		this.mapper = mapper;
		this.feedbackRepository = feedbackRepository;
	}
	
	@Override
	protected void mapSpecificFieldsInEntity(Feedback source, FeedbackDto destination) {
	}
	
	@Override
	protected void mapSpecificFieldsInDto(FeedbackDto source, Feedback destination) {
	}
}

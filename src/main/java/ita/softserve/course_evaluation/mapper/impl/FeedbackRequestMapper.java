package ita.softserve.course_evaluation.mapper.impl;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.mapper.AbstractMapper;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackRequestMapper extends AbstractMapper<FeedbackRequest, FeedbackRequestDto> {
	
	private final ModelMapper mapper;
	private final FeedbackRequestRepository feedbackRequestRepository;
	
	@Autowired
	public FeedbackRequestMapper(ModelMapper mapper, FeedbackRequestRepository feedbackRequestRepository) {
		super(FeedbackRequest.class, FeedbackRequestDto.class);
		this.mapper = mapper;
		this.feedbackRequestRepository = feedbackRequestRepository;
	}
	
	@Override
	protected void mapSpecificFieldsInEntity(FeedbackRequest source, FeedbackRequestDto destination) {
	
	}
	
	@Override
	protected void mapSpecificFieldsInDto(FeedbackRequestDto source, FeedbackRequest destination) {
	
	}
}

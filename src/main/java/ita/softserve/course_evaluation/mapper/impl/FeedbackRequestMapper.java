package ita.softserve.course_evaluation.mapper.impl;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.mapper.AbstractMapper;
import ita.softserve.course_evaluation.mapper.Mapper;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import ita.softserve.course_evaluation.repository.FeedbackRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

@Component
public class FeedbackRequestMapper extends AbstractMapper<FeedbackRequest, FeedbackRequestDto> {
	
	private final ModelMapper mapper;
	private final CourseRepository courseRepository;
	
	@Autowired
	public FeedbackRequestMapper(ModelMapper mapper, FeedbackRequestRepository feedbackRequestRepository, FeedbackRepository feedbackRepository, CourseRepository courseRepository, Mapper<Feedback, FeedbackDto> feedbackMapper) {
		super(FeedbackRequest.class, FeedbackRequestDto.class);
		this.mapper = mapper;
		this.courseRepository = courseRepository;
	}
	
	@PostConstruct
	public void setupMapper() {
		mapper.createTypeMap(FeedbackRequest.class, FeedbackRequestDto.class)
				.addMappings(mapping -> mapping.skip(FeedbackRequestDto::setCourse))
				.setPostConverter(toDtoConverter());
		mapper.createTypeMap(FeedbackRequestDto.class, FeedbackRequest.class)
				.addMappings(mapping -> mapping.skip(FeedbackRequest::setCourse))
				.addMappings(mapping -> mapping.skip(FeedbackRequest::setFeedbacks))
				.setPostConverter(toEntityConverter());
	}
	
	@Override
	protected void mapSpecificFieldsInEntity(FeedbackRequest source, FeedbackRequestDto destination) {
		destination.setCourse(source.getCourse().getId());
	}
	
	@Override
	protected void mapSpecificFieldsInDto(FeedbackRequestDto source, FeedbackRequest destination) {
		destination.setCourse(getCourse(source));
	}
	
	
	private Course getCourse(FeedbackRequestDto source) {
		return courseRepository.findById(source.getCourse())
				       .orElseThrow(() -> new EntityNotFoundException("Course not found."));
	}
	
}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackDto;

public interface FeedbackService {
	FeedbackDto create(FeedbackDto dto);
	
	FeedbackDto getFeedbackById(Long id);
}

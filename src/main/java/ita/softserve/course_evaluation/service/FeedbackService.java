package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackDto;

public interface FeedbackService {
	void create(FeedbackDto dto);
	
	void delete(Long id);
	
	void update(FeedbackDto dto);
	
	FeedbackDto getFeedbackById(Long id);
}

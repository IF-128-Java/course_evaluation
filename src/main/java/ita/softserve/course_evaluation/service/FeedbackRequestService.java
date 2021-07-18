package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;

public interface FeedbackRequestService {
	void create(FeedbackRequestDto dto);
	
	void update(FeedbackRequestDto dto);
	
	void delete(Long id);
	
	FeedbackRequestDto getFeedbackById(Long id);
}

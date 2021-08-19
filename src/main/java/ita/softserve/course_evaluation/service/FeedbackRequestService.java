package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;

public interface FeedbackRequestService {
	FeedbackRequestDto create(FeedbackRequestDto dto);
	
	FeedbackRequestDto update(FeedbackRequestDto dto);
	
	void delete(Long id);
	
	FeedbackRequestDto getFeedbackRequestById(Long id);
}

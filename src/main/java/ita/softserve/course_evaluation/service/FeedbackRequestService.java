package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface FeedbackRequestService {
	FeedbackRequestDto create(FeedbackRequestDto dto);
	
	FeedbackRequestDto update(FeedbackRequestDto dto);
	
	void delete(Long id);
	
	FeedbackRequestDto getFeedbackRequestById(Long id);
	
	Page<FeedbackRequestDto> findAllByCourseId(Pageable pageable, Long id);

	List<FeedbackRequestDto> getFeedbackRequestByCourseIdOnly(long id);
	
	List<FeedbackRequestDto> findAllByStatusActiveAndValidDate(long id);
	
	void changeStatusAndLastNotification(FeedbackRequestDto dto, int status);
}

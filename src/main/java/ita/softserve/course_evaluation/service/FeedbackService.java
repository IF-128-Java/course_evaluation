package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
    FeedbackDto create(FeedbackDto dto);

    FeedbackDto getFeedbackById(Long id);

    Page<FeedbackDto> findAllByFeedbackRequestId(Pageable pageable, Long id);
}

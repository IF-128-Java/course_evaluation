package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackDto;

import java.util.List;

public interface FeedbackService {
    FeedbackDto create(FeedbackDto dto);

    FeedbackDto getFeedbackById(Long id);

    List<FeedbackDto> findAllByFeedbackRequestId(Long id);
}

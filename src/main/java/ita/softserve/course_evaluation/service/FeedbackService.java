package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService {

    FeedbackDto create(FeedbackDto dto);

    FeedbackDto getFeedbackById(Long id);

    Page<FeedbackDto> findAllByFeedbackRequestId(Pageable pageable, Long id);

    List<Feedback> getFeedbackByRequestIdAndStudentId(long requestId, long studentId);
}

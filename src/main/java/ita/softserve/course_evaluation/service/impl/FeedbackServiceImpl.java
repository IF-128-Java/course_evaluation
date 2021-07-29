package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.FeedbackDtoMapper;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import ita.softserve.course_evaluation.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	private final AnswerToFeedbackService answerToFeedbackService;
	
	public FeedbackServiceImpl(FeedbackRepository feedbackRepository, AnswerToFeedbackService answerToFeedbackService) {
		this.feedbackRepository = feedbackRepository;
		this.answerToFeedbackService = answerToFeedbackService;
	}
	
	@Override
	public FeedbackDto create(FeedbackDto dto) {
		List<AnswerDto> answers = dto.getAnswers();
		List<AnswerDto> answersFromDb = answerToFeedbackService.saveAnswers(answers);
		Feedback feedbackFromDb = feedbackRepository.save(FeedbackDtoMapper.fromDto(dto));
		return FeedbackDtoMapper.toDto(feedbackFromDb, answersFromDb);
	}
	
	@Override
	public FeedbackDto getFeedbackById(Long id) {
		List<AnswerDto> answersFromDb = answerToFeedbackService.getAllAnswerByFeedbackId(id);
		Feedback feedbackFromDb = feedbackRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Feedback with id " + id + " not found"));
		return FeedbackDtoMapper.toDto(feedbackFromDb, answersFromDb);
	}
}

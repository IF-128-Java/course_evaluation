package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.FeedbackDtoMapper;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.repository.FeedbackRepository;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import ita.softserve.course_evaluation.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	private final AnswerToFeedbackService answerToFeedbackService;
	private final QuestionRepository questionRepository;
	
	public FeedbackServiceImpl(FeedbackRepository feedbackRepository, AnswerToFeedbackService answerToFeedbackService, QuestionRepository questionRepository) {
		this.feedbackRepository = feedbackRepository;
		this.answerToFeedbackService = answerToFeedbackService;
		this.questionRepository = questionRepository;
	}
	
	@Override
	public FeedbackDto create(FeedbackDto dto) {
		dto.setDate(LocalDateTime.now());
		List<AnswerDto> answers = dto.getAnswers();
		List<Long> longsIds = questionRepository.findAllQuestionIdsByFeedbackRequest(dto.getFeedbackRequestId());
		Feedback feedbackFromDb = feedbackRepository.save(FeedbackDtoMapper.fromDto(dto));
		filterAnswers(answers, longsIds, feedbackFromDb);
		List<AnswerDto> answersFromDb = answerToFeedbackService.saveAnswers(answers);
		return FeedbackDtoMapper.toDto(feedbackFromDb, answersFromDb);
	}
	
	private List<AnswerDto> filterAnswers(List<AnswerDto> answers, List<Long> longsIds, Feedback feedbackFromDb) {
		List<AnswerDto> notContainsAnswers = answers.stream()
				                                     .filter(answerDto -> !longsIds.contains(answerDto.getQuestionId()))
				                                     .collect(Collectors.toList());
		for(AnswerDto answerDto: notContainsAnswers){
			answers.remove(answerDto);
			log.warn("Answer is not related to this feedback request {}", answerDto);
		}
		
		List<Long> differences = longsIds.stream()
				                         .filter(element -> !answers.stream()
						                                             .map(AnswerDto::getQuestionId)
						                                             .collect(Collectors.toList())
						                                             .contains(element))
				                         .collect(Collectors.toList());
		if (!differences.isEmpty()) {
			for (Long id : differences) {
				AnswerDto answerDto = new AnswerDto();
				answerDto.setRate(0);
				answerDto.setFeedbackId(feedbackFromDb.getId());
				answerDto.setQuestionId(id);
				answers.add(answerDto);
				log.warn("Answer not found for question with id = {}. Set default value for this question", id);
			}
		}
		
		return answers.stream()
				       .peek(answer -> answer.setFeedbackId(feedbackFromDb.getId()))
				       .collect(Collectors.toList());
	}
	
	@Override
	public FeedbackDto getFeedbackById(Long id) {
		List<AnswerDto> answersFromDb = answerToFeedbackService.getAllAnswerByFeedbackId(id);
		Feedback feedbackFromDb = feedbackRepository.findById(id)
				                          .orElseThrow(() -> new EntityNotFoundException("Feedback with id " + id + " not found"));
		return FeedbackDtoMapper.toDto(feedbackFromDb, answersFromDb);
	}

	@Override
	public List<FeedbackDto> findAllByFeedbackRequestId(Long id) {
		List<Feedback> feedbacks = feedbackRepository.findAllFeedbackByFeedbackRequestId(id);
		return feedbacks.stream().map(f -> FeedbackDtoMapper.toDto(f, answerToFeedbackService.getAllAnswerByFeedbackId(f.getId()))).collect(Collectors.toList());
	}
}

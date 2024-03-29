package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.AnswerDtoMapper;
import ita.softserve.course_evaluation.repository.AnswerToFeedbackRepository;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerToFeedbackServiceImpl implements AnswerToFeedbackService {
	private final AnswerToFeedbackRepository answerRepository;
	
	@Autowired
	public AnswerToFeedbackServiceImpl(AnswerToFeedbackRepository answerRepository) {
		this.answerRepository = answerRepository;
	}
	
	@Override
	public List<AnswerDto> getAllAnswer() {
		return AnswerDtoMapper.toDto(answerRepository.findAll());
	}
	
	@Override
	public List<AnswerDto> getAllAnswerByFeedbackId(Long id) {
		return AnswerDtoMapper.toDto(answerRepository.findAll())
				       .stream()
				       .filter(answer -> answer.getFeedbackId().equals(id))
				       .collect(Collectors.toList());
	}
	
	@Override
	public AnswerDto saveAnswer(AnswerDto answer) {
		answerRepository.save(AnswerDtoMapper.fromDto(answer));
		return answer;
	}
	
	@Override
	public AnswerDto findAnswerById(long id) {
		return AnswerDtoMapper.toDto(answerRepository.findById(id)
				                             .orElseThrow(() -> new EntityNotFoundException("Answer was not found for id: " + id)));
	}
	
	@Override
	public List<AnswerDto> saveAnswers(List<AnswerDto> answer) {
		return AnswerDtoMapper.toDto(answerRepository.saveAll(AnswerDtoMapper.fromDto(answer)));
	}
	
	@Override
	public void deleteAnswerById(long id) {
		answerRepository.delete(AnswerDtoMapper.fromDto(findAnswerById(id)));
	}
	
	@Override
	public AnswerDto updateAnswer(AnswerDto answer, long id) {
		AnswerDto existingAnswerDto = findAnswerById(id);
		existingAnswerDto.setRate(answer.getRate());
		existingAnswerDto.setQuestionId(answer.getQuestionId());
		existingAnswerDto.setFeedbackId(answer.getFeedbackId());
		answerRepository.save(AnswerDtoMapper.fromDto(existingAnswerDto));
		return existingAnswerDto;
	}
}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.AnswerDtoMapper;
import ita.softserve.course_evaluation.entity.AnswerToFeedback;
import ita.softserve.course_evaluation.repository.AnswerToFeedbackRepository;
import ita.softserve.course_evaluation.service.impl.AnswerToFeedbackServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerToFeedbackServiceImplTests {

    @Mock
    private AnswerToFeedbackRepository answerToFeedbackRepository;

    @InjectMocks
    private AnswerToFeedbackServiceImpl answerToFeedbackService;

    @Test
    public void testGetAllAnswer(){
        try (MockedStatic<AnswerDtoMapper> mockedStatic = mockStatic(AnswerDtoMapper.class)) {
            List<AnswerToFeedback> answers = Collections.singletonList(new AnswerToFeedback());
            List<AnswerDto> expected = Collections.singletonList(new AnswerDto());

            when(answerToFeedbackRepository.findAll()).thenReturn(answers);
            mockedStatic.when(() -> AnswerDtoMapper.toDto(answers)).thenReturn(expected);

            List<AnswerDto> actual = answerToFeedbackService.getAllAnswer();

            assertEquals(expected, actual);

            verify(answerToFeedbackRepository, times(1)).findAll();
            mockedStatic.verify(() -> AnswerDtoMapper.toDto(answers), times(1));
            verifyNoMoreInteractions(answerToFeedbackRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testSaveAnswer(){
        try (MockedStatic<AnswerDtoMapper> mockedStatic = mockStatic(AnswerDtoMapper.class)) {
            AnswerToFeedback answer = new AnswerToFeedback();
            AnswerDto expected = new AnswerDto();

            mockedStatic.when(() -> AnswerDtoMapper.fromDto(expected)).thenReturn(answer);
            when(answerToFeedbackRepository.save(answer)).thenReturn(answer);

            AnswerDto actual = answerToFeedbackService.saveAnswer(expected);

            assertEquals(expected, actual);

            mockedStatic.verify(() -> AnswerDtoMapper.fromDto(expected), times(1));
            verify(answerToFeedbackRepository, times(1)).save(answer);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(answerToFeedbackRepository);
        }
    }

    @Test
    public void testCorrectFindAnswerById(){
        try (MockedStatic<AnswerDtoMapper> mockedStatic = mockStatic(AnswerDtoMapper.class)) {
            AnswerToFeedback answer = new AnswerToFeedback();
            AnswerDto expected = new AnswerDto();

            when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.of(answer));
            mockedStatic.when(() -> AnswerDtoMapper.toDto(answer)).thenReturn(expected);

            AnswerDto actual = answerToFeedbackService.findAnswerById(anyLong());

            assertEquals(expected, actual);

            verify(answerToFeedbackRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> AnswerDtoMapper.toDto(answer), times(1));
            verifyNoMoreInteractions(answerToFeedbackRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testExceptionFindAnswerById(){
        try (MockedStatic<AnswerDtoMapper> mockedStatic = mockStatic(AnswerDtoMapper.class)) {
            when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.empty());

            Throwable exception = assertThrows(EntityNotFoundException.class, () -> answerToFeedbackService.findAnswerById(anyLong()));

            assertEquals(String.format("Answer was not found for id: %d", 0), exception.getMessage());

            verify(answerToFeedbackRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> AnswerDtoMapper.toDto(any(AnswerToFeedback.class)), never());
            verifyNoMoreInteractions(answerToFeedbackRepository);
        }
    }

    @Test
    public void testDeleteAnswerById(){
        try (MockedStatic<AnswerDtoMapper> mockedStatic = mockStatic(AnswerDtoMapper.class)) {
            AnswerToFeedback answer = new AnswerToFeedback();
            AnswerDto answerDto = new AnswerDto();

            when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.of(answer));
            mockedStatic.when(() -> AnswerDtoMapper.toDto(answer)).thenReturn(answerDto);
            mockedStatic.when(() -> AnswerDtoMapper.fromDto(answerDto)).thenReturn(answer);
            doNothing().when(answerToFeedbackRepository).delete(answer);

            answerToFeedbackService.deleteAnswerById(anyLong());

            verify(answerToFeedbackRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> AnswerDtoMapper.toDto(answer), times(1));
            mockedStatic.verify(() -> AnswerDtoMapper.fromDto(answerDto), times(1));
            verify(answerToFeedbackRepository, times(1)).delete(answer);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(answerToFeedbackRepository);
        }
    }

    @Test
    public void testUpdateAnswer(){
        try (MockedStatic<AnswerDtoMapper> mockedStatic = mockStatic(AnswerDtoMapper.class)) {
            AnswerToFeedback answer = new AnswerToFeedback();
            AnswerDto expected = new AnswerDto();

            when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.of(answer));
            mockedStatic.when(() -> AnswerDtoMapper.toDto(answer)).thenReturn(expected);
            mockedStatic.when(() -> AnswerDtoMapper.fromDto(expected)).thenReturn(answer);
            when(answerToFeedbackRepository.save(answer)).thenReturn(answer);

            AnswerDto actual = answerToFeedbackService.updateAnswer(expected, anyLong());

            assertEquals(expected, actual);

            verify(answerToFeedbackRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> AnswerDtoMapper.toDto(answer), times(1));
            mockedStatic.verify(() -> AnswerDtoMapper.fromDto(expected), times(1));
            verify(answerToFeedbackRepository, times(1)).save(answer);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(answerToFeedbackRepository);
        }
    }
}
package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.AnswerDtoMapper;
import ita.softserve.course_evaluation.entity.AnswerToFeedback;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.AnswerToFeedbackRepository;
import ita.softserve.course_evaluation.service.impl.AnswerToFeedbackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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

    private static AnswerToFeedback expected;
    private static AnswerToFeedback expected2;
    private static AnswerDto expectedDto1;

    @BeforeAll
    public static void beforeAll(){
        Feedback feedback1 = new Feedback();
        feedback1.setId(1L);
        Feedback feedback2 = new Feedback();
        feedback2.setId(2L);

        expected = new AnswerToFeedback();
        expected.setId(1L);
        expected.setRate(10);
        expected.setQuestion(Question.builder().id(1L).build());
        expected.setFeedback(feedback1);

        expected2 = new AnswerToFeedback();
        expected2.setId(1L);
        expected2.setRate(10);
        expected2.setQuestion(Question.builder().id(2L).build());
        expected2.setFeedback(feedback2);

        expectedDto1 = AnswerDtoMapper.toDto(expected);
    }

    @AfterEach
    public void afterEach(){
        verifyNoMoreInteractions(answerToFeedbackRepository);
    }

    @Test
    public void testGetAllAnswer(){
        when(answerToFeedbackRepository.findAll()).thenReturn(List.of(expected));

        List<AnswerDto> actual = answerToFeedbackService.getAllAnswer();

        assertFalse(actual.isEmpty());

        verify(answerToFeedbackRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAnswer(){
        when(answerToFeedbackRepository.save(any())).thenReturn(expected);

        AnswerDto actual = answerToFeedbackService.saveAnswer(expectedDto1);

        assertEquals(expectedDto1, actual);

        verify(answerToFeedbackRepository, times(1)).save(any());
    }

    @Test
    public void testCorrectFindAnswerById(){
        when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        AnswerDto actual = answerToFeedbackService.findAnswerById(anyLong());

        assertEquals(expectedDto1.getId(), actual.getId());

        verify(answerToFeedbackRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testExceptionFindAnswerById(){
        when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> answerToFeedbackService.findAnswerById(anyLong()));

        assertEquals(String.format("Answer was not found for id: %d", 0), exception.getMessage());

        verify(answerToFeedbackRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteAnswerById(){
        when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        doNothing().when(answerToFeedbackRepository).delete(expected);

        answerToFeedbackService.deleteAnswerById(anyLong());

        verify(answerToFeedbackRepository, times(1)).findById(anyLong());
        verify(answerToFeedbackRepository, times(1)).delete(expected);
    }

    @Test
    public void testUpdateAnswer(){
        when(answerToFeedbackRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(answerToFeedbackRepository.save(any())).thenReturn(expected);

        AnswerDto actual = answerToFeedbackService.updateAnswer(expectedDto1, anyLong());

        assertEquals(expected.getId(), actual.getId());

        verify(answerToFeedbackRepository, times(1)).findById(anyLong());
        verify(answerToFeedbackRepository, times(1)).save(any());
    }

    @Test
    public void testGetAllAnswerByFeedbackId(){
        List<AnswerToFeedback> answerList = Arrays.asList(expected, expected2);
        when(answerToFeedbackRepository.findAll()).thenReturn(answerList);

        List<AnswerDto> expected = List.of(expectedDto1);
        List<AnswerDto> actual = answerToFeedbackService.getAllAnswerByFeedbackId(1L);

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(answerToFeedbackRepository).findAll();
        verifyNoMoreInteractions(answerToFeedbackRepository);
    }

    @Test
    void testSaveAnswersList() {
        when(answerToFeedbackRepository.saveAll(any())).thenReturn(List.of(expected,expected2));

        List<AnswerDto> expectedValue = AnswerDtoMapper.toDto(List.of(expected, expected2));
        List<AnswerDto> actual = answerToFeedbackService.saveAnswers(any());

        assertNotNull(expectedValue);
        assertArrayEquals(expectedValue.toArray(), actual.toArray());
        verify(answerToFeedbackRepository, times(1)).saveAll(any());
        verifyNoMoreInteractions(answerToFeedbackRepository);
    }
}
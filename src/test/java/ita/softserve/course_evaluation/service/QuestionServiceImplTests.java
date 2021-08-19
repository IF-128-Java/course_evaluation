package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTests {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private static Question expected;
    private static QuestionDto expectedDto;

    @BeforeAll
    public static void beforeAll(){
        expected = new Question();
        expected.setId(1L);
        expected.setQuestionText("Text");
        expected.setPattern(true);

        expectedDto = QuestionDtoMapper.toDto(expected);
    }

    @AfterEach
    public void afterEach(){
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    public void testGetAllQuestionIfExist(){
        when(questionRepository.findAll()).thenReturn(List.of(expected));

        List<QuestionDto> actual = questionService.getAllQuestion();

        assertFalse(actual.isEmpty());

        verify(questionRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllQuestionIfNotExist(){
        when(questionRepository.findAll()).thenReturn(Collections.emptyList());

        List<QuestionDto> actual = questionService.getAllQuestion();

        assertTrue(actual.isEmpty());

        verify(questionRepository, times(1)).findAll();
    }

    @Test
    public void testSaveQuestion(){
        when(questionRepository.save(any())).thenReturn(expected);

        Question actual = questionService.saveQuestion(expectedDto);

        assertEquals(expected.getId(), actual.getId());

        verify(questionRepository, times(1)).save(any());
    }

    @Test
    public void testCorrectFindQuestionById(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        QuestionDto actual = questionService.findQuestionById(anyLong());

        assertEquals(expected.getId(), actual.getId());

        verify(questionRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testExceptionFindQuestionById(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> questionService.findQuestionById(anyLong()));

        assertEquals(String.format("Question was not found for id: %d", 0), exception.getMessage());

        verify(questionRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteQuestionById(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        doNothing().when(questionRepository).delete(expected);

        questionService.deleteQuestionById(anyLong());

        verify(questionRepository, times(1)).findById(anyLong());
        verify(questionRepository, times(1)).delete(expected);
    }

    @Test
    public void testUpdateQuestion(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(questionRepository.save(any())).thenReturn(expected);

        QuestionDto actual = questionService.updateQuestion(expectedDto, anyLong());

        assertEquals(expectedDto.getId(), actual.getId());

        verify(questionRepository, times(1)).findById(anyLong());
        verify(questionRepository, times(1)).save(any());
    }
}
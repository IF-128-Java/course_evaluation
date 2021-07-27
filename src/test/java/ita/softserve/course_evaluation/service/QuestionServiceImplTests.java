package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.dto.QuestionDtoMapper;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.repository.QuestionRepository;
import ita.softserve.course_evaluation.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class QuestionServiceImplTests {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    public void testGetAllQuestionIfExist(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            List<Question> questions = Collections.singletonList(new Question());
            List<QuestionDto> expected = Collections.singletonList(new QuestionDto());

            when(questionRepository.findAll()).thenReturn(questions);
            mockedStatic.when(() -> QuestionDtoMapper.toDto(questions)).thenReturn(expected);

            List<QuestionDto> actual = questionService.getAllQuestion();

            assertEquals(expected, actual);

            verify(questionRepository, times(1)).findAll();
            mockedStatic.verify(() -> QuestionDtoMapper.toDto(questions), times(1));
            verifyNoMoreInteractions(questionRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testGetAllQuestionIfNotExist(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            List<Question> questionsEmptyList = Collections.emptyList();
            List<QuestionDto> questionDtoEmptyList = Collections.emptyList();

            when(questionRepository.findAll()).thenReturn(questionsEmptyList);
            mockedStatic.when(() -> QuestionDtoMapper.toDto(questionsEmptyList)).thenReturn(questionDtoEmptyList);

            List<QuestionDto> actual = questionService.getAllQuestion();

            assertTrue(actual.isEmpty());

            verify(questionRepository, times(1)).findAll();
            mockedStatic.verify(() -> QuestionDtoMapper.toDto(questionsEmptyList), times(1));
            verifyNoMoreInteractions(questionRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testSaveQuestion(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            QuestionDto questionDto = new QuestionDto();
            Question expected = new Question();

            mockedStatic.when(() -> QuestionDtoMapper.fromDto(questionDto)).thenReturn(expected);
            when(questionRepository.save(expected)).thenReturn(expected);

            Question actual = questionService.saveQuestion(questionDto);

            assertEquals(expected, actual);

            mockedStatic.verify(() -> QuestionDtoMapper.fromDto(questionDto), times(1));
            verify(questionRepository, times(1)).save(expected);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(questionRepository);
        }
    }

    @Test
    public void testCorrectFindQuestionById(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            Question question = new Question();
            QuestionDto expected = new QuestionDto();

            when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
            mockedStatic.when(() -> QuestionDtoMapper.toDto(question)).thenReturn(expected);

            QuestionDto actual = questionService.findQuestionById(anyLong());

            assertEquals(expected, actual);

            verify(questionRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> QuestionDtoMapper.toDto(question), times(1));
            verifyNoMoreInteractions(questionRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testExceptionFindQuestionById(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

            Throwable exception = assertThrows(RuntimeException.class, () -> questionService.findQuestionById(anyLong()));

            assertEquals(String.format("Question was not found for id: %d", 0), exception.getMessage());

            verify(questionRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> QuestionDtoMapper.toDto(any(Question.class)), never());
            verifyNoMoreInteractions(questionRepository);
        }
    }

    @Test
    public void testDeleteQuestionById(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            Question question = new Question();
            QuestionDto questionDto = new QuestionDto();

            when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
            mockedStatic.when(() -> QuestionDtoMapper.toDto(question)).thenReturn(questionDto);
            mockedStatic.when(() -> QuestionDtoMapper.fromDto(questionDto)).thenReturn(question);
            doNothing().when(questionRepository).delete(question);

            questionService.deleteQuestionById(anyLong());

            verify(questionRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> QuestionDtoMapper.toDto(question), times(1));
            mockedStatic.verify(() -> QuestionDtoMapper.fromDto(questionDto), times(1));
            verify(questionRepository, times(1)).delete(question);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(questionRepository);
        }
    }

    @Test
    public void testUpdateQuestion(){
        try (MockedStatic<QuestionDtoMapper> mockedStatic = mockStatic(QuestionDtoMapper.class)) {
            Question question = new Question();
            QuestionDto expected = new QuestionDto();

            when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
            mockedStatic.when(() -> QuestionDtoMapper.toDto(question)).thenReturn(expected);
            mockedStatic.when(() -> QuestionDtoMapper.fromDto(expected)).thenReturn(question);
            when(questionRepository.save(question)).thenReturn(question);

            QuestionDto actual = questionService.updateQuestion(expected, anyLong());

            assertEquals(expected, actual);

            verify(questionRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> QuestionDtoMapper.toDto(question), times(1));
            mockedStatic.verify(() -> QuestionDtoMapper.fromDto(expected), times(1));
            verify(questionRepository, times(1)).save(question);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(questionRepository);
        }
    }
}

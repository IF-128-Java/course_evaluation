package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.exception.CourseNotFoundException;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.service.impl.CourseServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTests {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    public void testAddCourse(){
        try (MockedStatic<CourseDtoMapper> mockedStatic = mockStatic(CourseDtoMapper.class)) {
            CourseDto courseDto = new CourseDto();
            Course expected = new Course();

            mockedStatic.when(() -> CourseDtoMapper.toEntity(courseDto)).thenReturn(expected);
            when(courseRepository.save(expected)).thenReturn(expected);

            Course actual = courseService.addCourse(courseDto);

            assertEquals(expected, actual);

            mockedStatic.verify(() -> CourseDtoMapper.toEntity(courseDto), times(1));
            verify(courseRepository, times(1)).save(expected);
            mockedStatic.verifyNoMoreInteractions();
            verifyNoMoreInteractions(courseRepository);
        }
    }

    @Test
    public void testDeleteById(){
        doNothing().when(courseRepository).deleteById(anyLong());

        courseService.deleteById(anyLong());

        verify(courseRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    public void testCorrectGetById(){
        try (MockedStatic<CourseDtoMapper> mockedStatic = mockStatic(CourseDtoMapper.class)) {
            Course course = new Course();
            CourseDto expected = new CourseDto();

            when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
            mockedStatic.when(() -> CourseDtoMapper.toDto(course)).thenReturn(expected);

            CourseDto actual = courseService.getById(anyLong());

            assertEquals(expected, actual);

            verify(courseRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> CourseDtoMapper.toDto(course), times(1));
            verifyNoMoreInteractions(courseRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testExceptionGetById(){
        try (MockedStatic<CourseDtoMapper> mockedStatic = mockStatic(CourseDtoMapper.class)) {
            when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(CourseNotFoundException.class, () -> courseService.getById(anyLong()));

            verify(courseRepository, times(1)).findById(anyLong());
            mockedStatic.verify(() -> CourseDtoMapper.toDto(any(Course.class)), never());
            verifyNoMoreInteractions(courseRepository);
        }
    }

    @Test
    public void testGetByName(){
        Optional<Course> expected = Optional.of(new Course());
        when(courseRepository.findByCourseName(anyString())).thenReturn(expected);

        Optional<Course> actual = courseService.getByName(anyString());

        assertEquals(expected, actual);

        verify(courseRepository, times(1)).findByCourseName(anyString());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    public void testCorrectEditCourse(){
        try (MockedStatic<CourseDtoMapper> mockedStatic = mockStatic(CourseDtoMapper.class)) {
            Course course = new Course();
            CourseDto expected = new CourseDto();

            when(courseRepository.existsById(anyLong())).thenReturn(true);
            mockedStatic.when(() -> CourseDtoMapper.toEntity(expected)).thenReturn(course);
            when(courseRepository.save(course)).thenReturn(course);
            mockedStatic.when(() -> CourseDtoMapper.toDto(course)).thenReturn(expected);

            CourseDto actual = courseService.editCourse(expected);

            assertEquals(expected, actual);

            verify(courseRepository, times(1)).existsById(anyLong());
            mockedStatic.verify(() -> CourseDtoMapper.toEntity(expected), times(1));
            verify(courseRepository, times(1)).save(course);
            mockedStatic.verify(() -> CourseDtoMapper.toDto(course), times(1));
            verifyNoMoreInteractions(courseRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testEditCourseIfNotExist(){
        try (MockedStatic<CourseDtoMapper> mockedStatic = mockStatic(CourseDtoMapper.class)) {
            when(courseRepository.existsById(anyLong())).thenReturn(false);

            CourseDto actual = courseService.editCourse(new CourseDto());

            assertNull(actual);

            verify(courseRepository, times(1)).existsById(anyLong());
            mockedStatic.verify(() -> CourseDtoMapper.toEntity(any(CourseDto.class)), never());
            verify(courseRepository, never()).save(any());
            mockedStatic.verify(() -> CourseDtoMapper.toDto(any(Course.class)), never());
            verifyNoMoreInteractions(courseRepository);
        }
    }

    @Test
    public void testGetAll(){
        try (MockedStatic<CourseDtoMapper> mockedStatic = mockStatic(CourseDtoMapper.class)) {
            List<Course> courses = Collections.singletonList(new Course());
            List<CourseDto> expected = Collections.singletonList(new CourseDto());

            when(courseRepository.findAll()).thenReturn(courses);
            mockedStatic.when(() -> CourseDtoMapper.toDto(courses)).thenReturn(expected);

            List<CourseDto> actual = courseService.getAll();

            assertEquals(expected, actual);

            verify(courseRepository, times(1)).findAll();
            mockedStatic.verify(() -> CourseDtoMapper.toDto(courses), times(1));
            verifyNoMoreInteractions(courseRepository);
            mockedStatic.verifyNoMoreInteractions();
        }
    }
}
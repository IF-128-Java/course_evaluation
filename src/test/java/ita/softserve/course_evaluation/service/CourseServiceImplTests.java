package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.exception.CourseNotFoundException;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

    private static Course expected;
    private static CourseDto expectedDto;

    @BeforeAll
    public static void beforeAll(){
        expected = new Course();
        expected.setId(1L);
        expected.setCourseName("Course Name");
        expected.setDescription("Description");
        expected.setStartDate(new Date());
        expected.setEndDate(new Date());

        expectedDto = CourseDtoMapper.toDto(expected);
    }

    @AfterEach
    public void afterEach(){
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    public void testAddCourse(){
        when(courseRepository.save(any())).thenReturn(expected);

        Course actual = courseService.addCourse(expectedDto);

        assertEquals(expected.getId(), actual.getId());

        verify(courseRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteById(){
        doNothing().when(courseRepository).deleteById(anyLong());

        courseService.deleteById(anyLong());

        verify(courseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testCorrectGetById(){
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        CourseDto actual = courseService.getById(anyLong());

        assertEquals(expectedDto.getId(), actual.getId());

        verify(courseRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testExceptionGetById(){
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.getById(anyLong()));

        verify(courseRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetByName(){
        when(courseRepository.findByCourseName(anyString())).thenReturn(Optional.of(expected));

        Optional<Course> actual = courseService.getByName(anyString());

        assertEquals(expected.getId(), actual.get().getId());

        verify(courseRepository, times(1)).findByCourseName(anyString());
    }

    @Test
    public void testCorrectEditCourse(){
        when(courseRepository.existsById(anyLong())).thenReturn(true);
        when(courseRepository.save(any())).thenReturn(expected);

        CourseDto actual = courseService.editCourse(expectedDto);

        assertEquals(expectedDto.getId(), actual.getId());

        verify(courseRepository, times(1)).existsById(anyLong());
        verify(courseRepository, times(1)).save(any());
    }

    @Test
    public void testEditCourseIfNotExist(){
        when(courseRepository.existsById(anyLong())).thenReturn(false);

        CourseDto actual = courseService.editCourse(expectedDto);

        assertNull(actual);

        verify(courseRepository, times(1)).existsById(anyLong());
        verify(courseRepository, never()).save(any());
    }

    @Test
    public void testGetAll(){
        when(courseRepository.findAll()).thenReturn(List.of(expected));

        List<CourseDto> actual = courseService.getAll();

        assertFalse(actual.isEmpty());

        verify(courseRepository, times(1)).findAll();
    }
}
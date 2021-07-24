package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CourseDtoMapper {

    private CourseDtoMapper() {  }

    public static Course toEntity(CourseDto courseDto) {

        if(courseDto == null) return null;

        Course course = new Course();
        course.setId(courseDto.getId());
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());

        return course;
    }

    public static CourseDto toDto(Course course) {

        if(course == null) return null;

        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setCourseName(course.getCourseName());
        courseDto.setDescription(course.getDescription());
        courseDto.setStartDate(course.getStartDate());
        courseDto.setEndDate(course.getEndDate());

        return courseDto;
    }

    public static List<Course> toEntity(List<CourseDto> coursesDto) {
        return Objects.isNull(coursesDto) ? null : coursesDto.stream().map(CourseDtoMapper::toEntity).collect(Collectors.toList());
    }

    public static List<CourseDto> toDto(List<Course> courses) {
        return Objects.isNull(courses) ? null : courses.stream().map(CourseDtoMapper::toDto).collect(Collectors.toList());
    }
}
package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CourseDtoMapper {

    private static ModelMapper mapper;

    public CourseDtoMapper() {

    }

    public Course toEntity(CourseDto courseDto) {
        return Objects.isNull(courseDto) ? null : mapper.map(courseDto, Course.class);
    }

    public static CourseDto toDto(Course course) {
        return Objects.isNull(course) ? null : mapper.map(course, CourseDto.class);
    }

    public List<CourseDto> toDto(List<Course> courses) {
        CourseDtoMapper courseDtoMapper = new CourseDtoMapper();
        return Objects.isNull(courses) ? null : courses.stream().map(CourseDtoMapper::toDto).collect(Collectors.toList());
    }
}
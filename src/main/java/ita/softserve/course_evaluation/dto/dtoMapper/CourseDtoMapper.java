package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CourseDtoMapper {

    private ModelMapper mapper;

    public CourseDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CourseDtoMapper() {

    }

    public Course toEntity(CourseDto courseDto) {
        return Objects.isNull(courseDto) ? null : mapper.map(courseDto, Course.class);
    }

    public CourseDto toDto(Course course) {
        return Objects.isNull(course) ? null : mapper.map(course, CourseDto.class);
    }
}

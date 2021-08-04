package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addCourse(CourseDto course);

    void deleteById(long id);

    CourseDto getById(long id);

    List<Course> getByName(String name);

    CourseDto editCourse(CourseDto courseDto);

    List<CourseDto> getAll();
}

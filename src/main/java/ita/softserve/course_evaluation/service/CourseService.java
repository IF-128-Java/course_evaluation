package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addCourse(CourseDto course);

    void deleteById(int id);

    CourseDto getById(int id);

    Optional<Course> getByName(String name);

    CourseDto editCourse(CourseDto courseDto);

    List<CourseDto> getAll();
}

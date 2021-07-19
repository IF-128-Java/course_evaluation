package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course addCourse(CourseDto course);

    void delete(int id);

    CourseDto getById(int id);

    Optional<Course> getByName(String name);

    Course editCourse(Course course);

    List<Course> getAll();
}

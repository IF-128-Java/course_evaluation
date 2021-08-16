package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;

import java.util.List;

public interface CourseService {

    Course addCourse(CourseDto course);

    void deleteById(long id);

    CourseDto getById(long id);

    List<CourseDto> getByName(String courseName);

    CourseDto editCourse(CourseDto courseDto);

    List<CourseDto> getAll();

    List<CourseDto> finishedCoursesByGroupId(long id);

    List<CourseDto> currentCoursesByGroupId(long id);
}

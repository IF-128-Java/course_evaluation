package ita.softserve.course_evaluation.service.impl;


import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.CourseNotFoundException;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course addCourse(CourseDto courseDto) {
        return courseRepository.save(Course.builder()
                .courseName(courseDto.getCourseName())
                .description(courseDto.getDescription())
                .startDate(courseDto.getStartDate())
                .endDate(courseDto.getEndDate())
                .teacher(new User(courseDto.getTeacherId()))
                .build());
    }

    @Override
    public void deleteById(long id) {
        courseRepository.deleteById((id));
    }

    @Override
    public CourseDto getById(long id) {
        return CourseDtoMapper.toDto(courseRepository.findById(id).orElseThrow(CourseNotFoundException::new));
    }

    @Override
    public List<CourseDto> getByName(String courseName) {
        return CourseDtoMapper.toDto(courseRepository.findCourseByName(courseName));
    }

    @Override
    public CourseDto editCourse(CourseDto courseDto) {
        if(courseRepository.existsById(courseDto.getId())) {
            return CourseDtoMapper.toDto(courseRepository.save(CourseDtoMapper.toEntity(courseDto)));
        }
        return null;
    }

    @Override
    public List<CourseDto> getAll() {
        List<CourseDto> courses = CourseDtoMapper.toDto(courseRepository.findAllCourses());
        return Objects.isNull(courses) ? Collections.emptyList() : courses;
    }

    @Override
    public List<CourseDto> finishedCoursesByGroupId(long id) {
        List<CourseDto> courses = CourseDtoMapper.toDto(courseRepository.finishedCoursesOfGroup(id));
        return Objects.isNull(courses) ? Collections.emptyList() : courses;
    }

    @Override
    public List<CourseDto> currentCoursesByGroupId(long id) {
        List<CourseDto> courses = CourseDtoMapper.toDto(courseRepository.currentCoursesOfGroup(id));
        return Objects.isNull(courses) ? Collections.emptyList() : courses;
    }

}

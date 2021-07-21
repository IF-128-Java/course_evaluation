package ita.softserve.course_evaluation.service.impl;


import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.exception.CourseNotFoundException;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseDtoMapper courseDtoMapper = new CourseDtoMapper();


    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course addCourse(CourseDto course) {
        String name = course.getCourseName();
        courseRepository.findByCourseName(name).ifPresent(n -> {
            throw new RuntimeException(name + ": this course already created");
        });
        return courseRepository.save(courseDtoMapper.toEntity(course));
    }

    @Override
    public void deleteById(int id) {
        courseRepository.delete(courseDtoMapper.toEntity(getById(id)));
    }

    @Override
    public CourseDto getById(int id) {
        return CourseDtoMapper.toDto(courseRepository.findById(id).orElseThrow(CourseNotFoundException::new));
    }

    @Override
    public Optional<Course> getByName(String name) {
        return courseRepository.findByCourseName(name);
    }

    @Override
    public CourseDto editCourse(CourseDto courseDto) {
        getById(courseDto.getId());
        return CourseDtoMapper.toDto(courseRepository.save(courseDtoMapper.toEntity(courseDto)));
    }

    @Override
    public List<CourseDto> getAll() {
        List<CourseDto> courses = courseDtoMapper.toDto((List<Course>) courseRepository.findAll());
        return courses.isEmpty() ? Collections.emptyList() : courses;
    }

}

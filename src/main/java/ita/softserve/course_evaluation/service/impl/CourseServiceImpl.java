package ita.softserve.course_evaluation.service.impl;


import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course addCourse(CourseDto course) {
        CourseDtoMapper courseDtoMapper = new CourseDtoMapper();
        String name = course.getCourseName();
        courseRepository.findByCourseName(name).ifPresent(n -> {
            throw new RuntimeException(name + ": this course already created");
        });
        return courseRepository.save(courseDtoMapper.toEntity(course));
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Course getById(int id) {
        return null;
    }

    @Override
    public Optional<Course> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Course editCourse(Course course) {
        return null;
    }

    @Override
    public List<Course> getAll() {
        return null;
    }

//    @Autowired
//    CourseRepository courseRepository;
//
//    @Transactional
//    @Override
//    public void addCourse() {
//        Course courseJava = new Course();
//    }
}

package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.repository.CourseRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("api/course")
//@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @RequestMapping(method = {RequestMethod.POST})
    public void postCourse(@RequestBody CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        courseRepository.save(course);
    }

    @GetMapping("/api/course/{id}")
}

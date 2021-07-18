package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.exception.CourseNotFoundException;
import ita.softserve.course_evaluation.repository.CourseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses/")
//@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;
    private Object CourseNotFoundException;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public void postCourse(@RequestBody CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public void putCourse(@PathVariable int id, @RequestBody CourseDto courseDto) {
        Course course = new Course();
        course.setId(id);
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        courseRepository.save(course);
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable int id) {
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    @GetMapping
    public List<Course> getCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseRepository.deleteById(id);
    }
}

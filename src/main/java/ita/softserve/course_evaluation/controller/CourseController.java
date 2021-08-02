package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.service.CourseService;
import ita.softserve.course_evaluation.swagger.api.CourseApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/courses/")
public class CourseController implements CourseApi {
    private final CourseService courseService;

    public CourseController(CourseService courseService) { this.courseService = courseService; }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseDto courseDto) {
        return Objects.isNull(courseDto) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.addCourse(courseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable int id) {
        return Objects.isNull(courseService.getById(id)) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses() {
        return Objects.isNull(courseService.getAll()) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.getAll());
    }


    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable int id, @RequestBody CourseDto courseDto) {
        return Objects.isNull(courseService.editCourse(courseDto)) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.editCourse(courseDto));
    }


    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseService.deleteById(id);
    }
}

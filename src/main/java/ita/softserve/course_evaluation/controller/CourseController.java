package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.CourseDto;
import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.service.CourseService;
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

@Api(tags = "Course service REST API")
@RestController
@RequestMapping("api/v1/courses/")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) { this.courseService = courseService; }

    @ApiOperation(value = "Create new Course")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = Course.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseDto courseDto) {
        return Objects.isNull(courseDto) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.addCourse(courseDto));
    }

    @ApiOperation(value = "Get course by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable long id) {
        return Objects.isNull(courseService.getById(id)) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.getById(id));
    }

    @ApiOperation(value = "Get course by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("name/{courseName}")
    public ResponseEntity<List<CourseDto>> getCourseByName(@PathVariable String courseName) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getByName(courseName));
    }

    @ApiOperation(value = "Get All Courses List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getAll());
    }

    @ApiOperation(value = "Update Course")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable long id, @RequestBody CourseDto courseDto) {
        courseDto.setId(id);
        return Objects.isNull(courseService.editCourse(courseDto)) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(courseService.editCourse(courseDto));
    }

    @ApiOperation(value = "Delete Course by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable long id) {
        courseService.deleteById(id);
    }

    @ApiOperation(value = "Get finished courses of group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/group/{id}")
    public ResponseEntity<List<CourseDto>> finisheCoursesByGroupId(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.getFinishedCoursesByGroupId(id));
    }

    @ApiOperation(value = "Get current courses of group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/current/group/{id}")
    public ResponseEntity<List<CourseDto>> currentCoursesByGroupId(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.getCurrentCoursesByGroupId(id));
    }

    @ApiOperation(value = "Get list of available courses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/available/")
    public ResponseEntity<List<CourseDto>> getAvailableCourses() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.getAvailableCourses());
    }
}

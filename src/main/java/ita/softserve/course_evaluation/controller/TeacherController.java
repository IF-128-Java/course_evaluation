package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.TeacherToCourseDto;
import ita.softserve.course_evaluation.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Api(tags = "Course service REST API")
@RestController
@RequestMapping("api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @ApiOperation(value = "Get All Teachers List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = TeacherToCourseDto.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping
    public ResponseEntity<List<TeacherToCourseDto>> getTeachers() {
        return Objects.isNull(teacherService.getAllTeachers()) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(teacherService.getAllTeachers());
    }

    @ApiOperation(value = "Get teacher by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = TeacherToCourseDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeacherToCourseDto> getCourseById(@PathVariable long id) {
        return Objects.isNull(teacherService.getTeacherById(id)) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherById(id));
    }
}

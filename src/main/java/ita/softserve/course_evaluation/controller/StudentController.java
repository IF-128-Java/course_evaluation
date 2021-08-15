package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import ita.softserve.course_evaluation.dto.StudentDto;
import ita.softserve.course_evaluation.service.StudentService;
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
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable long id) {
        return Objects.isNull(studentService.getById(id)) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(studentService.getById(id));
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<List<StudentDto>> getStudentsByGroupId(@PathVariable long id) {
        return Objects.isNull(studentService.getStudentsByGroupId(id)) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByGroupId(id));
    }
}
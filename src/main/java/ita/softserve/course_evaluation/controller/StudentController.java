package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.MailDto;
import ita.softserve.course_evaluation.dto.StudentDto;
import ita.softserve.course_evaluation.service.StudentService;
import ita.softserve.course_evaluation.service.mail.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.AddressException;
import java.util.List;
import java.util.Objects;

@Api(tags = "Course service REST API")
@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final EmailService emailService;

    public StudentController(StudentService studentService, EmailService emailService) {
        this.studentService = studentService;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Get Student by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = StudentDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable long id) {
          return ResponseEntity.status(HttpStatus.OK).body(studentService.getById(id));
    }


    @ApiOperation(value = "Get list of Students from group with ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping("/group/{id}")
    public ResponseEntity<List<StudentDto>> getStudentsByGroupId(@PathVariable long id) {
          return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByGroupId(id));
    }
    
    @ApiOperation(value = "Get All Students by course id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    
    @GetMapping("/course/{id}")
    public ResponseEntity<List<StudentDto>> getStudentsByCourseId(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByCourseId(id));
    }


    @ApiOperation(value = "Send mail to selected students")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = MailDto.class),
            @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR),
    })
    @PostMapping("/mail")
    public ResponseEntity<MailDto> sendMailToSelectedStudents(@RequestBody MailDto mailDto) {

        try {
            emailService.sendSimpleEmail(mailDto);
        } catch (MailException | AddressException mailException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mailDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(mailDto);
    }
}

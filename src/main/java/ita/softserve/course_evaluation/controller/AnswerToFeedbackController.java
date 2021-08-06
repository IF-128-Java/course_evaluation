package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Answer service REST API")
@RestController
@RequestMapping("api/v1/answers")
public class AnswerToFeedbackController {

    private final AnswerToFeedbackService answerService;

    @Autowired
    public AnswerToFeedbackController(AnswerToFeedbackService answerService) {
        this.answerService = answerService;
    }

    @ApiOperation(value = "Get All Answer List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<AnswerDto>> getAllAnswers() {
        final List<AnswerDto> answerList = answerService.getAllAnswer();
        return answerList != null && !answerList.isEmpty()
                ? new ResponseEntity<>(answerList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "Create new Answer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PostMapping
    public ResponseEntity<AnswerDto> addAnswer(AnswerDto answerDto) {
        return new ResponseEntity<>(answerService.saveAnswer(answerDto), HttpStatus.CREATED);
    }


    @ApiOperation(value = "Get Answer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("{id}")
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable("id") long id) {
        return new ResponseEntity<>(answerService.findAnswerById(id), HttpStatus.OK);
    }


    @ApiOperation(value = "Delete Answer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable("id") long id) {
        answerService.deleteAnswerById(id);
        return new ResponseEntity<>("Answer deleted successfully.", HttpStatus.OK);
    }


    @ApiOperation(value = "Update Answer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PutMapping("{id}")
    public ResponseEntity<AnswerDto> updateAnswer(@RequestBody AnswerDto dto, @PathVariable("id") long id) {
        return new ResponseEntity<>(answerService.updateAnswer(dto, id), HttpStatus.OK);
    }
}

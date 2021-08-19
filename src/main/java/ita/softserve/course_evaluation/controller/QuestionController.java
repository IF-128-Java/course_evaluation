package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiParam;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Question service REST API")
@RestController
@RequestMapping("api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @ApiOperation("Get All Questions List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        final List<QuestionDto> questions = questionService.getAllQuestion();
        return questions != null && !questions.isEmpty()
                ? new ResponseEntity<>(questions, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation("Create new Question")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED, response = QuestionDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Question> addQuestion(@ApiParam(value = "Need question in json format")
                                                @RequestBody QuestionDto question) {
        return new ResponseEntity<>(questionService.saveQuestion(question),
                HttpStatus.CREATED);
    }

    @ApiOperation("Get Question by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable("id") long questionId) {
        return new ResponseEntity<>(questionService.findQuestionById(questionId), HttpStatus.OK);
    }

    @ApiOperation("Update Question")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable("id") long id
            , @RequestBody QuestionDto question) {
        return new ResponseEntity<>(questionService.updateQuestion(question, id), HttpStatus.OK);
    }

    @ApiOperation("Delete Question")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("id") long id) {
        questionService.deleteQuestionById(id);
        return new ResponseEntity<>("Question deleted successfully!.", HttpStatus.OK);
    }

}

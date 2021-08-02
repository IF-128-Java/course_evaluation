package ita.softserve.course_evaluation.swagger.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.entity.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Question management REST API")
@ApiResponses({
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR)
})
public interface QuestionApi {

    @ApiOperation("Get All Questions List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<QuestionDto>> getAllQuestions();

    @ApiOperation("Create new Question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Question> addQuestion(@ApiParam(value = "Need question in json format")
                                         @RequestBody QuestionDto question);

    @ApiOperation("Get Question by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<QuestionDto> getQuestionById(@PathVariable("id") long questionId);

    @ApiOperation("Update Question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<QuestionDto> updateQuestion(@PathVariable("id") long id
            , @RequestBody QuestionDto question);

    @ApiOperation("Delete Question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    ResponseEntity<String> deleteQuestion(@PathVariable("id") long id);

}

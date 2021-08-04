package ita.softserve.course_evaluation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.dto.QuestionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api(tags = "Answer service REST API")
@ApiResponses({
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR)
})
public interface AnswerApi {

    @ApiOperation(value = "Get All Answer List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = QuestionDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<AnswerDto>> getAllAnswers();

    @ApiOperation(value = "Create new Answer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    ResponseEntity<AnswerDto> addAnswer(AnswerDto answerDto);

    @ApiOperation(value = "Get Answer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    ResponseEntity<AnswerDto> getAnswerById(@ApiParam(value = "Require long Id parameter") @PathVariable("id") long id);

    @ApiOperation(value = "Delete Answer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    ResponseEntity<String> deleteAnswer(@PathVariable("id") long id);

    @ApiOperation(value = "Update Answer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = AnswerDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    ResponseEntity<AnswerDto> updateAnswer(@RequestBody AnswerDto dto, @PathVariable("id") long id);
}

package ita.softserve.course_evaluation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "Feedback service REST API")
@ApiResponses({
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackDto.class),
        @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR),
})
public interface FeedbackApi {

    @ApiOperation(value = "Create new Feedback")
    ResponseEntity<FeedbackDto> createFeedback(@ApiParam(value = "Require FeedbackDto as parameter") @RequestBody FeedbackDto dto);

    @ApiOperation(value = "Get Feedback by Id")
    ResponseEntity<FeedbackDto> getFeedback(@ApiParam(value = "Require Long Id as parameter") @PathVariable Long id);

}

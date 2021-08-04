package ita.softserve.course_evaluation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.FeedbackRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Api(tags = "FeedbackRequest service REST API")
@ApiResponses({
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackDto.class),
        @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR),
})
public interface FeedbackRequestApi {

    @ApiOperation(value = "Create FeedbackRequest")
    ResponseEntity<FeedbackRequestDto> createFeedbackRequest(@RequestBody FeedbackRequestDto dto);

    @ApiOperation(value = "Get FeedbackRequest by Id")
    ResponseEntity<FeedbackRequestDto> getFeedbackRequest(@PathVariable Long id);

    @ApiOperation(value = "Update FeedbackRequest")
    ResponseEntity<FeedbackRequestDto> editFeedbackRequest(@RequestBody FeedbackRequestDto dto, @PathVariable Long id);

    @ApiOperation(value = "Delete FeedbackRequest by Id")
    void deleteFeedbackRequest(@PathVariable Long id);
}

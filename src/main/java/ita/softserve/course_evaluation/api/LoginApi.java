package ita.softserve.course_evaluation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "Login service REST API")
@ApiResponses({
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR),
})
public interface LoginApi {

    @ApiOperation(value = "Authenticate user")
    ResponseEntity<?> authenticate(@RequestBody AuthenticateRequestDto request);

    @ApiOperation(value = "Logout")
    void logout(HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Registration")
    ResponseEntity<?> registration(@RequestBody SimpleUserDto request);

}

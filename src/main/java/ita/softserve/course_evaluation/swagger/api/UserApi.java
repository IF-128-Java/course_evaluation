package ita.softserve.course_evaluation.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "User service REST API")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = FeedbackDto.class),
        @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR),
})
public interface UserApi {

    @ApiOperation(value = "Get All Users List")
    ResponseEntity<List<UserDto>> read();

    @ApiOperation(value = "Get User by ID")
    ResponseEntity<UserDto> readById(@PathVariable(value="id") long id);

    @ApiOperation(value = "Get User by Username")
    ResponseEntity<UserDto> readByName(@RequestParam String name);

    @ApiOperation(value = "Create new User")
    ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto);

    @ApiOperation(value = "Update User")
    ResponseEntity<UserDto> updateUser (@RequestBody UserDto userDto);

    @ApiOperation("Delete User by Id")
    void deleteUser(@PathVariable(value = "id") long id);

}

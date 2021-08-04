package ita.softserve.course_evaluation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.FeedbackDto;
import ita.softserve.course_evaluation.dto.GroupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api(tags = "Group service REST API")
@ApiResponses({
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
        @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
        @ApiResponse(code = 500, message = HttpStatuses.INTERNAL_SERVER_ERROR),
})
public interface GroupApi {

    @ApiOperation(value = "Get All Group List")
    ResponseEntity<List<GroupDto>> getAllGroups();

    @ApiOperation(value = "Get Group by Id")
    ResponseEntity<GroupDto> getGroupById(@PathVariable("id") long id);

    @ApiOperation(value = "Delete Group")
    ResponseEntity<GroupDto> deleteGroup(@PathVariable long id);

    @ApiOperation(value = "Create new Group")
    ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto groupdto);

    @ApiOperation(value = "Update Group")
    ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto groupdto);
}

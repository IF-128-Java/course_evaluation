package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Group service REST API")
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @ApiOperation(value = "Get all Group list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Group by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable("id") long id) {
        return new ResponseEntity<>(groupService.getById(id), HttpStatus.OK);

    }
}
package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.dto.GroupDtoMapper;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @ApiOperation(value = "Get All Group List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups() {

        final List<GroupDto> groups = groupService.getAll();

        return groups != null && !groups.isEmpty()
                ? new ResponseEntity<>(groups, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Get Group by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable("id") long id) {

        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final GroupDto group = groupService.getById(id);

        return group != null
                ? new ResponseEntity<>(group, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Delete Group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GroupDto> deleteGroup(@PathVariable long id) {
        final GroupDto group = groupService.getById(id);

        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Create new Group")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PostMapping
    public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto groupdto) {

        if (groupdto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group group = groupService.create(groupdto);

        return group != null
                ? new ResponseEntity<>(GroupDtoMapper.entityToDto(group), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Update Group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PutMapping
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto groupdto) {

        if (groupdto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GroupDto group = groupService.update(groupdto);

        return group != null
                ? new ResponseEntity<>(group, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
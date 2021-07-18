package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.dto.GroupDtoMapper;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupDtoMapper groupDtoMapper;

    public GroupController(GroupService groupService, GroupDtoMapper groupDtoMapper) {
        this.groupService = groupService;
        this.groupDtoMapper = groupDtoMapper;
    }

    @GetMapping(value = "/groups")
    public ResponseEntity<List<GroupDto>> getAllGroups() {

        final List<Group> groups = groupService.getAll();

        return groups != null && !groups.isEmpty()
                ? new ResponseEntity<>(groupDtoMapper.entityToDto(groups), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/groups/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable("id") long id) {

        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Group group = groupService.getById(id);

        return group != null
                ? new ResponseEntity<>(groupDtoMapper.entityToDto(group), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Group> deleteGroup(@PathVariable long id) {
        final Group group = groupService.getById(id);

        if(group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto groupdto) {

        if (groupdto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group group = groupService.save(groupDtoMapper.dtoToEntity(groupdto));

        return ResponseEntity.status(HttpStatus.OK).body(groupDtoMapper.entityToDto(group));
    }

    @PutMapping("/groups")
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto groupdto) {

        if (groupdto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group group = groupService.save(groupDtoMapper.dtoToEntity(groupdto));

        return ResponseEntity.status(HttpStatus.OK).body(groupDtoMapper.entityToDto(group));
    }

}
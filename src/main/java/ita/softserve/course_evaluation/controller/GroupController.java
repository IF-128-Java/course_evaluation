package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.dto.GroupDtoMapper;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupDtoMapper groupDtoMapper;

    public GroupController(GroupService groupService, GroupDtoMapper groupDtoMapper) {
        this.groupService = groupService;
        this.groupDtoMapper = groupDtoMapper;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<GroupDto>> getAllGroups() {

        final List<Group> groups = groupService.getAll();

        return groups != null && !groups.isEmpty()
                ? new ResponseEntity<>(groupDtoMapper.entityToDto(groups), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable("id") long id) {

        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Group group = groupService.getById(id);

        return group != null
                ? new ResponseEntity<>(groupDtoMapper.entityToDto(group), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Group> deleteGroup(@PathVariable long id) {
        final Group group = groupService.getById(id);

        if(group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto groupdto) {

        if (groupdto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group group = groupService.create(groupDtoMapper.dtoToEntity(groupdto));

        return group != null
                ? new ResponseEntity<>(groupDtoMapper.entityToDto(group), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("")
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto groupdto) {

        if (groupdto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group group = groupService.update(groupDtoMapper.dtoToEntity(groupdto));

        return group != null
                ? new ResponseEntity<>(groupDtoMapper.entityToDto(group), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
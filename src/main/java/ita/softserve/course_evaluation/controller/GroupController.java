package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    public GroupController(GroupService theGroupService)
    {
        this.groupService = theGroupService;
    }

    @GetMapping(value = "/groups/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable("id") long id) {

        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Group group = groupService.getById(id);

        return group != null
                ? new ResponseEntity<>(group, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/groups")
    public ResponseEntity<List<Group>> getAllGroups() {

        final List<Group> groups = groupService.getAll();

        return groups != null && !groups.isEmpty()
                ? new ResponseEntity<>(groups, HttpStatus.OK)
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
    public ResponseEntity<Group> addGroup(@RequestBody Group group) {
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(groupService.save(group));
    }

    @PutMapping("/groups")
    public ResponseEntity<Group> updateGroup(@RequestBody Group group) {

        if (group == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(groupService.save(group));
    }

}
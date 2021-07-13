package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.GroupService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    public GroupController(GroupService roleService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/groups")
    public ResponseEntity<List<Group>> read() {
        final List<Group> groups = groupService.getAllGroup();

        return groups != null && !groups.isEmpty()
                ? new ResponseEntity<>(groups, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable long id) {
        System.out.println(id);

        final Group group = groupService.readById(id);

        return group != null
                ? new ResponseEntity<>(group, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/groups/{id}")
    void deletePermission(@PathVariable long id) {
        groupService.deleteGroup(id);
    }

    @PutMapping("/groups")
    public ResponseEntity<Group> updateGroup(@RequestBody Group group) {

        return ResponseEntity.status(HttpStatus.OK).body(groupService.update(group));
    }

    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.addGroup(group));
    }
}
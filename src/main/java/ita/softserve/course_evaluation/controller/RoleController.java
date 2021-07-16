package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.RoleDto;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.service.RoleService;
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
@RequestMapping("api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getPermissions() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getPermissionById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleService.readById(id));
    }

    @PostMapping
    public ResponseEntity<Role> addPermission(@RequestBody RoleDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.create(dto));
    }

    @DeleteMapping("/{id}")
    void deletePermission(@PathVariable long id) {
        roleService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> editPermission(@RequestBody RoleDto dto, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.update(dto));
    }

}

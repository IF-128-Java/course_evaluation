package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.PermissionDto;
import ita.softserve.course_evaluation.dto.RoleDto;
import ita.softserve.course_evaluation.entity.Permission;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("/all")
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
        dto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(roleService.update(dto));
    }

}

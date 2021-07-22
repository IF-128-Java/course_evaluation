package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.PermissionDto;
import ita.softserve.course_evaluation.entity.Permission;
import ita.softserve.course_evaluation.service.PermissionService;
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
@RequestMapping("api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<PermissionDto>> getPermissions() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionService.readById(id));
    }

    @GetMapping("role/{id}")
    public ResponseEntity<List<PermissionDto>> getPermissionByRoleId(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionService.getByRoleId(id));
    }

    @PostMapping
    public ResponseEntity<Permission> addPermission(@RequestBody PermissionDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.create(dto));
    }

    @DeleteMapping("/{id}")
    void deletePermission(@PathVariable long id) {
        permissionService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDto> editPermission(@RequestBody PermissionDto dto, @PathVariable long id) {
        dto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.update(dto));
    }
}

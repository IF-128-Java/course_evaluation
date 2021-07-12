package ita.softserve.course_evaluation.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleDto {
    private Long id;
    private String roleName;
    private List<PermissionDto> permissions = new ArrayList<>();

    public RoleDto() {
    }

    public RoleDto(Long id, String roleName, List<PermissionDto> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
    }
}

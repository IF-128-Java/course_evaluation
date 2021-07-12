package ita.softserve.course_evaluation.dto;


import ita.softserve.course_evaluation.entity.Permission;

import java.util.List;

public class PermissionDto {

    private Long id;

    private String permissionName;

    private String description;

    private Long role_id;

    public PermissionDto() {
    }

    public PermissionDto(Long id, String permissionName, String description, Long role_id) {
        this.id = id;
        this.permissionName = permissionName;
        this.description = description;
        this.role_id = role_id;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }
}

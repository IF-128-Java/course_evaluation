package ita.softserve.course_evaluation.dto;


import java.util.Objects;

public class PermissionDto {

    private Long id;

    private String permissionName;

    private Long role_id;

    public PermissionDto() {
    }

    public PermissionDto(Long id, String permissionName, Long role_id) {
        this.id = id;
        this.permissionName = permissionName;
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

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionDto that = (PermissionDto) o;
        return Objects.equals(id, that.id) && Objects.equals(permissionName, that.permissionName) && Objects.equals(role_id, that.role_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permissionName, role_id);
    }

    @Override
    public String toString() {
        return "PermissionDto{" +
                "id=" + id +
                ", permissionName='" + permissionName + '\'' +
                ", role_id=" + role_id +
                '}';
    }
}

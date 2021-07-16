package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Permission;
import ita.softserve.course_evaluation.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionDtoMapper {

    public static Permission fromDto(PermissionDto dto) {
        Permission permission = null;
        if (dto != null) {
            Role role = new Role();
            role.setId(dto.getRole_id());
            permission = new Permission(dto.getId(), dto.getPermissionName(), role);
        }
        return permission;
    }

    public static List<Permission> fromDto(List<PermissionDto> dto) {
        return dto == null ? null : dto.stream().map(PermissionDtoMapper::fromDto).collect(Collectors.toList());
    }

    public static PermissionDto toDto(Permission permission) {
        return permission == null ? null : new PermissionDto(permission.getId(), permission.getPermissionName(), permission.getRole().getId());
    }

    public static List<PermissionDto> toDto(List<Permission> permissions) {
        return permissions == null ? null : permissions.stream().map(PermissionDtoMapper::toDto).collect(Collectors.toList());
    }
}

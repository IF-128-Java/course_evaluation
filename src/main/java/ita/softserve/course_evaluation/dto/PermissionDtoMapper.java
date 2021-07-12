package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Permission;
import ita.softserve.course_evaluation.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionDtoMapper {

    public static Permission fromDto(PermissionDto dto){
        Permission permission = new Permission(dto.getPermissionName(), dto.getDescription());
        permission.setId(dto.getId());
        Role role= new Role();
        role.setId(dto.getRole_id());
        permission.setRole(role);
        return permission;
    }
    public static List<Permission> fromDto(List<PermissionDto> dto){
        return dto.stream().map(PermissionDtoMapper::fromDto).collect(Collectors.toList());
    }

    public static PermissionDto toDto(Permission permission){
        return new PermissionDto(permission.getId(), permission.getPermissionName(), permission.getDescription(), permission.getRole().getId());
    }
    public static List<PermissionDto> toDto(List<Permission> permissions){
        return permissions.stream().map(PermissionDtoMapper::toDto).collect(Collectors.toList());
    }
}

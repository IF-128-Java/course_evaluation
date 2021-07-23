package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.ERole;
import ita.softserve.course_evaluation.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDtoMapper {
    public static Role fromDto(RoleDto dto) {
        return dto == null ? null : new Role(dto.getId(), ERole.valueOf(dto.getRoleName().toUpperCase()), PermissionDtoMapper.fromDto(dto.getPermissions()));
    }

    public static List<Role> fromDto(List<RoleDto> dto) {
        return dto == null ? null : dto.stream().map(RoleDtoMapper::fromDto).collect(Collectors.toList());
    }

    public static RoleDto toDto(Role role) {
        return role == null ? null : new RoleDto(role.getId(), role.getRoleName().name(), PermissionDtoMapper.toDto(role.getPermissions()));
    }

    public static List<RoleDto> toDto(List<Role> roles) {
        return roles == null ? null : roles.stream().map(RoleDtoMapper::toDto).collect(Collectors.toList());
    }
}
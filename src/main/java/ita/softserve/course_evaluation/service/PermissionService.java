package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.PermissionDto;
import ita.softserve.course_evaluation.entity.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    Permission create(PermissionDto dto);

    PermissionDto readById(long id);

    PermissionDto update(PermissionDto dto);

    void delete(long id);

    List<PermissionDto> getAll();

    List<PermissionDto> getByRoleId(long id);

    Optional<Permission> findByName(String name);

}

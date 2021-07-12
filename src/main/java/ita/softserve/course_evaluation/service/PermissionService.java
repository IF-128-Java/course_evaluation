package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    Permission create(Permission permission);

    Permission readById(long id);

    Permission update(Permission permission);

    void delete(long id);

    List<Permission> getAll();

    List<Permission> getByRoleId(long id);

    Optional<Permission> findByName(String name);

}

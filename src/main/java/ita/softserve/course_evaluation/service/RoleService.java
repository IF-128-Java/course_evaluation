package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.RoleDto;
import ita.softserve.course_evaluation.entity.ERole;
import ita.softserve.course_evaluation.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role create(RoleDto dto);

    RoleDto readById(long id);

    RoleDto update(RoleDto dto);

    void delete(long id);

    List<RoleDto> getAll();

    Optional<Role> findByName(String name);

}

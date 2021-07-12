package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.ERole;
import ita.softserve.course_evaluation.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role create(Role role);

    Role readById(long id);

    Role update(Role role);

    void delete(long id);

    List<Role> getAll();

    Optional<Role> findByName(ERole role);

}

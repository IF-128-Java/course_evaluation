package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ERole;
import ita.softserve.course_evaluation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(ERole roleName);
}

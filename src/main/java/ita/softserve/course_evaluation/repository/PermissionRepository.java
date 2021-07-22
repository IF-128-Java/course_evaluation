package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionName(String name);

    List<Permission> findPermissionsByRole_Id(long id);
}

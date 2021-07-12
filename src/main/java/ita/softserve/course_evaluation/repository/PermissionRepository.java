package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionName(String name);

    @Query(value = " select p.id, p.permission_name, p.description from permission p inner join role_permission rp on p.id = rp.permission_id where rp.role_id = ?1",nativeQuery = true)
    List<Permission> findAllByRoleId(long id);
}

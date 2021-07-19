package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findGroupByGroupName(String groupName);
}
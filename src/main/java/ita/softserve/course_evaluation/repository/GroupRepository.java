package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
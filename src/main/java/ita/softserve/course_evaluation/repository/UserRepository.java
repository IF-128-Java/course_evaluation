package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
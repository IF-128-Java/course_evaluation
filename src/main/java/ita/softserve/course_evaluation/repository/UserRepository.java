package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(long id);
    Optional<User> findUserByFirstName(String firstName);
}
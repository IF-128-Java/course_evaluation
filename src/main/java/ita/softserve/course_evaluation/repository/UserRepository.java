package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUserByFirstName(String firstName);

    Optional<User> findUserByEmail (String email);
    boolean existsByEmail(String email);
}
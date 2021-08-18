package ita.softserve.course_evaluation.registration;

import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivaUserRepository extends JpaRepository<UserActive, Long> {
    UserActive getByUser(User user);

}

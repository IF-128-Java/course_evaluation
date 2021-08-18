package ita.softserve.course_evaluation.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivaUserRepository extends JpaRepository<UserActive, Long> {

}

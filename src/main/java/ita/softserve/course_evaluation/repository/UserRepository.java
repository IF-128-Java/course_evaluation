package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT id, first_name, last_name, email, password, group_id, profile_picture, account_verified, role_id, user_id " +
            "FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = 1", nativeQuery = true)
    List<User> getAllTeachersByRole();

    @Query(value = "SELECT id, first_name, last_name, email, password, group_id, profile_picture, account_verified, role_id, user_id " +
            "FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE u.id = :id", nativeQuery = true)
    User getTeacherById(@Param("id") long id);

    @Query(value = "SELECT id, first_name, last_name, email, password, group_id, profile_picture, account_verified FROM users u WHERE u.group_id = :id ORDER BY last_name ASC, first_name ASC", nativeQuery = true)
    List<User> getStudentsByGroupId(@Param("id") long id);

    Optional<User> findUserById(long id);

    List<User> findUserByFirstName(String firstName);

    Optional<User> findUserByEmail (String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT u.* FROM course_group cg INNER JOIN users u on u.group_id=cg.group_id WHERE cg.course_id = :id", nativeQuery = true)
	List<User> getStudentsByCourseId(long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users " +
            "SET account_verified = TRUE WHERE email = :email", nativeQuery = true)
    void enableAppUser(String email);

    @Query(value = "SELECT u.* FROM course_group cg " +
                           "INNER JOIN users u ON cg.group_id = u.group_id " +
                           "INNER JOIN course_feedback_request cfr ON cg.course_id = cfr.course_id " +
                           "LEFT JOIN course_feedback cf ON u.id = cf.student_id " +
                           "WHERE cfr.id = :id " +
                           "AND (cfr.id <> cf.feedback_request_id OR cf.id IS NULL)" +
                           "AND (cfr.status=1 OR cfr.status=2)" +
                           "AND (CURRENT_DATE >= CAST(cfr.start_date AS DATE) AND CURRENT_DATE <= CAST(cfr.end_date AS DATE))", nativeQuery = true)
    Page<User> findAllUserByFeedbackRequestIdWithoutFeedback(Pageable pageable, long id);
}
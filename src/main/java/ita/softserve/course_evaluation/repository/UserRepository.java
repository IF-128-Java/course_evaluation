package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
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

    @Query(value = "SELECT id, first_name, last_name, email, password, group_id, profile_picture, role_id, user_id FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = 1", nativeQuery = true)
    List<User> getAllTeachersByRole();

    @Query(value = "SELECT id, first_name, last_name, email, password, group_id, profile_picture, role_id, user_id FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE u.id = :id", nativeQuery = true)
    User getTeacherById(@Param("id") long id);

    @Query(value = "SELECT id, first_name, last_name, email, password, group_id, profile_picture, account_verified FROM users u WHERE u.group_id = :id ORDER BY last_name ASC, first_name ASC", nativeQuery = true)
    List<User> getStudentsByGroupId(@Param("id") long id);

    Optional<User> findUserById(long id);

    List<User> findUserByFirstName(String firstName);

    Optional<User> findUserByEmail (String email);

    boolean existsByEmail(String email);
    
    @Query(value = "SELECT u.id, u.first_name, u.last_name, u.email, u.password, u.group_id , u.profile_picture\n" +
                           "FROM course_group cg\n" +
                           "INNER JOIN users u on u.group_id=cg.group_id\n" +
                           "WHERE cg.course_id = :id", nativeQuery = true)
	List<User> getStudentsByCourseId(long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users " +
            "SET account_verified = TRUE WHERE email = :email", nativeQuery = true)
    void enableAppUser(String email);
}
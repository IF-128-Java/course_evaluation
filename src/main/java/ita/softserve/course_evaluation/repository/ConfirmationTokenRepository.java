package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE confirmation_token as c " +
            "SET confirmed_at = :confirmedAt " +
            "WHERE token = :token", nativeQuery = true)
    int updateConfirmedAt(@Param("token") String token,
                          @Param("confirmedAt") LocalDateTime confirmedAt);


    ConfirmationToken findByAppUser(User user);

}

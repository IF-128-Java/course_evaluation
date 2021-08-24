package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query(value = "UPDATE confirmation_token as c " +
            "SET confirmed_at = :confirmedAt " +
            "WHERE token = :token", nativeQuery = true)
    void updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);


    ConfirmationToken findByAppUser(User user);

}

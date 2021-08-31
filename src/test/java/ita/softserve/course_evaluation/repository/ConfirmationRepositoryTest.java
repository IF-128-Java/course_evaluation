package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(value = false)
@Transactional()
public class ConfirmationRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void testUpdateConfirmedAt() {

        User user = new User();
        user.setFirstName("testToken");
        user.setLastName("testToken");
        user.setPassword("test");
        user.setEmail("mail@mail");


        LocalDateTime createAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime expiredAt = createAt.plusMinutes(15);
        LocalDateTime confirmedAt = createAt.plusMinutes(10).truncatedTo(ChronoUnit.MILLIS);

        String token = UUID.randomUUID().toString();
        ConfirmationToken savedToken = confirmationTokenRepository.save(new ConfirmationToken(token, createAt, expiredAt, userRepository.save(user)));
        confirmationTokenRepository.updateConfirmedAt(savedToken.getToken(), confirmedAt);

        Session session = entityManager.unwrap(Session.class);
        NativeQuery<ConfirmationToken> query = session.createNativeQuery("select  * from confirmation_token where token = '" + token +"'", ConfirmationToken.class);
        List<ConfirmationToken> confirmationTokens = query.getResultList();
        System.out.println(confirmationTokens);

        assertEquals(1, confirmationTokens.size());
        assertEquals(createAt, confirmationTokens.get(0).getCreatedAt());
        assertEquals(expiredAt, confirmationTokens.get(0).getExpiredAt());
        assertEquals(token, confirmationTokens.get(0).getToken());
        assertEquals(confirmedAt, confirmationTokens.get(0).getConfirmedAt());
        assertEquals(user.getId(), confirmationTokens.get(0).getAppUser().getId());

    }

}

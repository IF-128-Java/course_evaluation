package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.ConfirmationTokenRepository;
import ita.softserve.course_evaluation.service.impl.ConfirmationTokenServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

/**
 * @author Mykhailo Fedenko on 31.08.2021
 */
@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceImplTest {
    private ConfirmationToken expected;
    private User user;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private ConfirmationTokenServiceImpl confirmationTokenService;

    @BeforeEach
    public void beforeEach(){
        user = new User();
        user.setId(1L);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("email@mail.com");
        user.setPassword("password");

        expected = new ConfirmationToken();
        expected.setToken("token");
        expected.setCreatedAt(LocalDateTime.now());
        expected.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        expected.setAppUser(user);
    }

    @AfterEach
    public void afterEach(){
        verifyNoMoreInteractions(confirmationTokenRepository);
    }

    @Test
    public void testSetConfirmedAt(){
        LocalDateTime confirmedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        lenient().when(confirmationTokenRepository.updateConfirmedAt(StringUtils.EMPTY,confirmedAt)).thenReturn(1);

        confirmationTokenService.setConfirmedAt(StringUtils.EMPTY);

        verify(confirmationTokenRepository).updateConfirmedAt(StringUtils.EMPTY, confirmedAt);
    }

    @Test
    public void testGetToken(){
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(expected));
        Optional<ConfirmationToken> actual = confirmationTokenService.getToken(anyString());

        assertEquals(expected.getToken(), actual.get().getToken());
        verify(confirmationTokenRepository).findByToken(anyString());
    }

    @Test
    public void testUpdateConfirmationTokenWithExistUser(){

        when(confirmationTokenRepository.findByAppUser(any(User.class))).thenReturn(expected);
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(expected);

        confirmationTokenService.updateConfirmationToken(user, expected);

        verify(confirmationTokenRepository, times(1)).findByAppUser(any(User.class));
        verify(confirmationTokenRepository, times(1)).save(any(ConfirmationToken.class));

    }

    @Test
    public void testUpdateConfirmationTokenWithNullUser(){

        when(confirmationTokenRepository.findByAppUser(any(User.class))).thenReturn(null);
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(expected);

        confirmationTokenService.updateConfirmationToken(user, expected);

        verify(confirmationTokenRepository, times(1)).findByAppUser(any(User.class));
        verify(confirmationTokenRepository, times(1)).save(any(ConfirmationToken.class));
    }

    @Test
    public void testSaveConfirmationToken(){

        when(confirmationTokenRepository.save(expected)).thenReturn(expected);
        ConfirmationToken confirmationToken = confirmationTokenService.saveConfirmationToken(expected);

        assertEquals(expected, confirmationToken);
        verify(confirmationTokenRepository, times(1)).save(expected);

    }
}

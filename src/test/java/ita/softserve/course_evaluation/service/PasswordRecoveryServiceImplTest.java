package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.PasswordRestoreDto;
import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.ConfirmationTokenException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.ConfirmationTokenService;
import ita.softserve.course_evaluation.service.impl.PasswordRecoveryServiceImpl;
import ita.softserve.course_evaluation.service.mail.EmailService;
import ita.softserve.course_evaluation.service.mail.context.AbstractEmailContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;

/**
 * @author Mykhailo Fedenko on 03.09.2021
 */
@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceImplTest {
    private PasswordRestoreDto passwordRestoreDto;
    private ConfirmationToken confirmationToken;
    private User user;

    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    PasswordRecoveryServiceImpl passwordRecoveryService;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(passwordRecoveryService, "baseUrl", "http://localhost:4200", String.class);

        user = new User();
        user.setId(1L);
        user.setFirstName("Mike");
        user.setLastName("Green");
        user.setEmail("mike@mail.com");
        user.setPassword("password");

        String token = UUID.randomUUID().toString();
        confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        passwordRestoreDto = PasswordRestoreDto.builder()
                .password("new_password")
                .confirmPassword("new_password")
                .token(token)
                .build();

    }

    @Test
    void testForgottenPasswordWithNotExistUser() {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> passwordRecoveryService.forgottenPassword(Mockito.anyString()));
    }

    @Test
    void testForgottenPasswordWithExistUser() throws MessagingException {

        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        when(confirmationTokenService.saveConfirmationToken(Mockito.any(ConfirmationToken.class))).thenReturn(confirmationToken);
        doNothing().when(emailService).sendMail(Mockito.any(AbstractEmailContext.class));

        passwordRecoveryService.forgottenPassword("mike@mail.com");

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(confirmationTokenService, times(1)).saveConfirmationToken(Mockito.any(ConfirmationToken.class));
        verify(emailService, times(1)).sendMail(Mockito.any(AbstractEmailContext.class));

    }

    @Test
    void testUpdatePasswordWithNotExistToken() {
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.empty());
        assertThrows(ConfirmationTokenException.class, () -> passwordRecoveryService.updatePassword(passwordRestoreDto));
    }

    @Test
    void testUpdatePasswordWithExistWrongToken() {
        passwordRestoreDto.setToken("wrong_token");
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.of(confirmationToken));
        assertThrows(ConfirmationTokenException.class, () -> passwordRecoveryService.updatePassword(passwordRestoreDto));
    }

    @Test
    void testUpdatePassword() {
        when(confirmationTokenService.getToken(Mockito.anyString())).thenReturn(Optional.of(confirmationToken));
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        passwordRecoveryService.updatePassword(passwordRestoreDto);

        verify(confirmationTokenService).getToken(Mockito.anyString());
        verify(userRepository).findUserById(Mockito.anyLong());
        verify(userRepository).save(Mockito.any(User.class));
        verify(passwordEncoder).encode(Mockito.anyString());
    }

}
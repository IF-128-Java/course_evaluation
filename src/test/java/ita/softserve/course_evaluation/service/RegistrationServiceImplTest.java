package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.SimpleUserDtoMapper;
import ita.softserve.course_evaluation.dto.SimpleUserDtoResponseMapper;
import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.EmailNotConfirmedException;
import ita.softserve.course_evaluation.exception.EmailNotValidException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.RegistrationServiceImpl;
import ita.softserve.course_evaluation.service.mail.DefaultEmailService;
import ita.softserve.course_evaluation.service.mail.context.AbstractEmailContext;
import ita.softserve.course_evaluation.validator.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Mykhailo Fedenko on 02.09.2021
 */

@ExtendWith({MockitoExtension.class})
@Disabled("Not implemented yet")
@ContextConfiguration(classes = ConfigDataApplicationContextInitializer.class)
class RegistrationServiceImplTest {
    @Value("${site.base.url.https}")
    private String baseUrl;

    private SimpleUserDto simpleUserDto;
    private SimpleUserDto simpleUserDto2;
    private User userMike;
    private User userNick;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private DefaultEmailService emailService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void beforeEach() {
        userMike = new User();
        userMike.setId(1L);
        userMike.setFirstName("Mike");
        userMike.setLastName("Green");
        userMike.setEmail("email@mail.com");
        userMike.setPassword("password");
        userMike.setAccountVerified(false);

        userNick = new User();
        userNick.setId(1L);
        userNick.setFirstName("Mike");
        userNick.setLastName("Green");
        userNick.setEmail("email@mail.com");
        userNick.setPassword("password");
        userNick.setAccountVerified(true);

        simpleUserDto = SimpleUserDtoMapper.toDto(userMike);
        simpleUserDto2 = SimpleUserDtoMapper.toDto(userNick);
    }

    @Test
    void testRegisterWhenEmailNotValid() {

        when(emailValidator.test(Mockito.anyString())).thenReturn(false);
        EmailNotValidException emailNotValidException = assertThrows(EmailNotValidException.class,
                () -> registrationService.register(simpleUserDto));

        assertEquals("Email not valid", emailNotValidException.getMessage());
        verify(emailValidator, times(1)).test(Mockito.anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder, emailValidator, emailService, confirmationTokenService);

    }

    @Test
    void testRegisterWhenEmailValidAndNotRegisteredUser() throws MessagingException {
        ReflectionTestUtils.setField(registrationService, "baseUrl", "http://localhost:4200", String.class);

        ConfirmationToken confirmationToken = new ConfirmationToken(UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userMike);

        when(emailValidator.test(Mockito.anyString())).thenReturn(true);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userMike);
        when(confirmationTokenService.saveConfirmationToken(Mockito.any(ConfirmationToken.class))).thenReturn(confirmationToken);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
        doNothing().when(emailService).sendMail(Mockito.any(AbstractEmailContext.class));

        ResponseEntity<?> actual = registrationService.register(simpleUserDto);

        verify(emailValidator, times(1)).test(anyString());
        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
        verify(confirmationTokenService, times(1)).saveConfirmationToken(Mockito.any(ConfirmationToken.class));
        verify(emailService, times(1)).sendMail(Mockito.any(AbstractEmailContext.class));
        verify(passwordEncoder, times(1)).encode(Mockito.anyString());
        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verifyNoMoreInteractions(userRepository,emailValidator,confirmationTokenService,passwordEncoder,emailService);

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(SimpleUserDtoResponseMapper.toDto(userMike), actual.getBody());

    }

    @Test
    void testRegisterWhenEmailValidAndAlreadyRegisteredUserWithNotVerifiedAccount() throws MessagingException {
        ReflectionTestUtils.setField(registrationService, "baseUrl", "http://localhost:4200", String.class);

        ConfirmationToken confirmationToken = new ConfirmationToken(UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userMike);

        when(emailValidator.test(Mockito.anyString())).thenReturn(true);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userMike));
        when(confirmationTokenService.saveConfirmationToken(Mockito.any(ConfirmationToken.class))).thenReturn(confirmationToken);
        doNothing().when(emailService).sendMail(Mockito.any(AbstractEmailContext.class));

        Exception actualResult = assertThrows(EmailNotConfirmedException.class,
                () -> registrationService.register(simpleUserDto));

        verify(emailValidator, times(1)).test(anyString());
        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
        verify(confirmationTokenService, times(1)).saveConfirmationToken(Mockito.any(ConfirmationToken.class));
        verify(emailService, times(1)).sendMail(Mockito.any(AbstractEmailContext.class));
        verifyNoMoreInteractions(userRepository,emailValidator,confirmationTokenService,passwordEncoder,emailService);

        assertEquals("Email already exist. Please confirm it", actualResult.getMessage());
    }

    @Test
    void testRegisterWhenEmailValidAndAlreadyRegisteredUserWithVerifiedAccount() throws MessagingException {
        ReflectionTestUtils.setField(registrationService, "baseUrl", "http://localhost:4200", String.class);

        when(emailValidator.test(Mockito.anyString())).thenReturn(true);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userNick));

        Exception actualResult = assertThrows(UserAlreadyExistAuthenticationException.class,
                () -> registrationService.register(simpleUserDto2));

        verify(emailValidator, times(1)).test(anyString());
        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
        verifyNoMoreInteractions(userRepository,emailValidator,confirmationTokenService,passwordEncoder,emailService);

        assertEquals("User already exist", actualResult.getMessage());
    }

    @Test
    @Disabled("Not implemented yet")
    void testSignUp() {
    }

    @Test
    @Disabled("Not implemented yet")
    void testConfirmToken() {
    }

    @Test
    @Disabled("Not implemented yet")
    void testEnableUserEmail() {
    }
}
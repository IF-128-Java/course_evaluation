package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.SimpleUserDtoMapper;
import ita.softserve.course_evaluation.dto.SimpleUserDtoResponseMapper;
import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.EmailAlreadyConfirmedException;
import ita.softserve.course_evaluation.exception.EmailNotConfirmedException;
import ita.softserve.course_evaluation.exception.EmailNotValidException;
import ita.softserve.course_evaluation.exception.ConfirmationTokenException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.RegistrationServiceImpl;
import ita.softserve.course_evaluation.service.mail.DefaultEmailService;
import ita.softserve.course_evaluation.service.mail.context.AbstractEmailContext;
import ita.softserve.course_evaluation.validator.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.reset;

/**
 * @author Mykhailo Fedenko on 02.09.2021
 */

@ExtendWith({MockitoExtension.class})
class RegistrationServiceImplTest {

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
        ReflectionTestUtils.setField(registrationService, "baseUrl", "http://localhost:4200", String.class);

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
    void testRegisterWhenEmailValidAndAlreadyRegisteredUserWithVerifiedAccount() {

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
    void testSignUp() {

        //test when user not registered
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(userMike);

        registrationService.signUp(userMike);
        verify(userRepository).save(Mockito.any(User.class));
        verify(userRepository).findUserByEmail(Mockito.anyString());
        verifyNoMoreInteractions(userRepository);

        //test signUp when user has not activate account
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userMike));
        assertThrows(EmailNotConfirmedException.class, () -> registrationService.signUp(userMike));
        verifyNoMoreInteractions(userRepository);

        //test signUp when user has activated account
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userNick));
        assertThrows(UserAlreadyExistAuthenticationException.class, () -> registrationService.signUp(userMike));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void testConfirmToken() {
        //test when given token is wrong
        reset(confirmationTokenService);
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.empty());
        assertThrows(ConfirmationTokenException.class, () -> registrationService.confirmToken(anyString()));

        //test when token already confirmed
        reset(confirmationTokenService);
        ConfirmationToken confirmationToken1 = new ConfirmationToken();
        confirmationToken1.setConfirmedAt(LocalDateTime.now().plusMinutes(5));
        when(confirmationTokenService.getToken(Mockito.anyString())).thenReturn(Optional.of(confirmationToken1));
        assertThrows(EmailAlreadyConfirmedException.class, () -> registrationService.confirmToken(anyString()));

        //test when token expired
        reset(confirmationTokenService);
        ConfirmationToken confirmationToken2 = new ConfirmationToken();
        confirmationToken2.setExpiredAt(LocalDateTime.now());
        when(confirmationTokenService.getToken(Mockito.anyString())).thenReturn(Optional.of(confirmationToken2));
        assertThrows(ConfirmationTokenException.class, () -> registrationService.confirmToken("token"));

        //test when token valid and not confirmed
        reset(confirmationTokenService);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken3 = new ConfirmationToken();
        confirmationToken3.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken3.setToken(token);
        confirmationToken3.setAppUser(userMike);
        when(confirmationTokenService.getToken(Mockito.anyString())).thenReturn(Optional.of(confirmationToken3));
        doNothing().when(confirmationTokenService).setConfirmedAt(anyString());

        ResponseEntity<?> responseEntity = registrationService.confirmToken(token);
        assertEquals("Email was confirmed", responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(confirmationTokenService, times(1)).getToken(token);
        verify(confirmationTokenService, times(1)).setConfirmedAt(token);
        verifyNoMoreInteractions(confirmationTokenService);
    }

    @Test
    void testEnableUserEmail() {
        doNothing().when(userRepository).enableAppUser(Mockito.anyString());
        registrationService.enableUserEmail("mail@mail.com");
        verify(userRepository).enableAppUser("mail@mail.com");
    }
}
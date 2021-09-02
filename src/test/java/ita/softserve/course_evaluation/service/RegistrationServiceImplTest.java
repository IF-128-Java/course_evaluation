package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.SimpleUserDtoMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.EmailNotValidException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.RegistrationServiceImpl;
import ita.softserve.course_evaluation.service.mail.DefaultEmailService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Mykhailo Fedenko on 02.09.2021
 */

@ExtendWith({MockitoExtension.class})
@Disabled("Not implemented yet")
class RegistrationServiceImplTest {
    @Value("${site.base.url.https}")
    private String baseUrl;
    private SimpleUserDto simpleUserDto;

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailValidator emailValidator;
    @Mock private DefaultEmailService emailService;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @InjectMocks private RegistrationServiceImpl registrationService;

    @BeforeEach
    void beforeEach(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("email@mail.com");
        user.setPassword("password");

        simpleUserDto = SimpleUserDtoMapper.toDto(user);
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
    @Disabled
    void testRegisterWhenEmailValid() {

    }

    @Test
    @Disabled
    void testSignUp() {
    }

    @Test
    @Disabled
    void testConfirmToken() {
    }

    @Test
    @Disabled
    void testEnableUserEmail() {
    }
}
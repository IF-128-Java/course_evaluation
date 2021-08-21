package ita.softserve.course_evaluation.registration;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.SimpleUserDtoResponseMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.JwtAuthenticationException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.registration.token.ConfirmationToken;
import ita.softserve.course_evaluation.registration.token.ConfirmationTokenService;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.MailSender;
import ita.softserve.course_evaluation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService{

    @Value("${site.base.url.https}")
    private String baseUrl;

    private final EmailValidator emailValidator;

    private final UserService userService;

    private final UserRepository userRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final MailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(EmailValidator emailValidator, UserService userService, UserRepository userRepository, ConfirmationTokenService confirmationTokenService, MailSender mailSender, PasswordEncoder passwordEncoder) {
        this.emailValidator = emailValidator;
        this.userService = userService;
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<?> register(SimpleUserDto request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        String token = signUp(user);

        String link = buildVerificationUrl(baseUrl, token);

        sendActivationMessage(user, link, "Course Evaluation activation");

        return ResponseEntity.ok(SimpleUserDtoResponseMapper.toDto(user));
    }

    @Override
    public String signUp(User user) {
        Optional<User> userExists = userRepository.findUserByEmail(user.getEmail());
        if (userExists.isPresent()){
            if(!userExists.get().isAccountVerified()){

                String mailToken = buildConfirmationToken(userExists.get());
                String link = buildVerificationUrl(baseUrl, mailToken);
                sendActivationMessage(user, link, "Course Evaluation activation");

                throw new UserAlreadyExistAuthenticationException("Email already exist. Please activate it");
            }
            throw new UserAlreadyExistAuthenticationException("User already exist");
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        userRepository.save(user);

        return buildConfirmationToken(user);
    }

    private String buildConfirmationToken(User user2) {
        String mailToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                mailToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user2
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return mailToken;
    }

    public String buildVerificationUrl(final String baseURL, final String token){
        return UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/confirm").queryParam("token", token).toUriString();
    }

    private void sendActivationMessage(User user, String link, String subject) {
        if (!user.getEmail().isBlank()) {
            String message = String.format(
                    "Hello, %s! \n" + "Your activation link: %s \nThis link will expire in 15 min",
                    user.getFirstName() + " " + user.getLastName(), link);
            mailSender.send(user.getEmail(), subject, message);
            log.info(message);
        }
    }

    @Transactional
    public ResponseEntity<?> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new UserAlreadyExistAuthenticationException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new JwtAuthenticationException("Token expired");
        }
        confirmationTokenService.setConfirmedAt(token);

        enableUserEmail(confirmationToken.getAppUser().getEmail());
        log.info("Token was confirmed");
        return new ResponseEntity<>("Email was confirmed", HttpStatus.OK);
    }

    public void enableUserEmail(String email) {
        userRepository.enableAppUser(email);
    }
}

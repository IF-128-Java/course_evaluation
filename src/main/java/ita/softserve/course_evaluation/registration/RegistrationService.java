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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {

    @Value("${site.base.url.https}")
    private String baseUrl;

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailSender mailSender;

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

        String token = userService.signUp(user);

        sendActivationMessage(user, baseUrl, token);

        return ResponseEntity.ok(SimpleUserDtoResponseMapper.toDto(user));
    }

    @Transactional
    public String confirmToken(String token) {
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
        userService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        log.info("Token was confirmed");
        return "confirmed";
    }

    private void sendActivationMessage(User user, String address, String token) {
        if (!user.getEmail().isBlank()) {
            String message = String.format(
                    "Hello, %s! \n" + "Your activation link: %s/api/v1/auth/confirm?token=%s",
                    user.getFirstName() + " " + user.getLastName(),
                    address,
                    token
            );
            mailSender.send(user.getEmail(), "Class Evaluation activation", message);
            log.info(message);
        }
    }
}

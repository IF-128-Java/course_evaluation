package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.SimpleUserDtoResponseMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.*;
import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.service.ConfirmationTokenService;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.RegistrationService;
import ita.softserve.course_evaluation.service.mail.EmailService;
import ita.softserve.course_evaluation.service.mail.context.AccountVerificationEmailContext;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpManager;
import ita.softserve.course_evaluation.validator.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${site.base.url.https}")
    private String baseUrl;

    @Autowired
    private TotpManager totpManager;

    private final EmailValidator emailValidator;

    private final UserRepository userRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(EmailValidator emailValidator, UserRepository userRepository, ConfirmationTokenService confirmationTokenService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.emailValidator = emailValidator;
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> register(SimpleUserDto request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new EmailNotValidException("Email not valid");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (request.isActive_2fa()){
            user.setActive2FA(true);
            user.setSecret(totpManager.generateSecret());
        }

        signUp(user);

        sendActivationMessage(user);



        return ResponseEntity.ok(SimpleUserDtoResponseMapper.toDto(user));
    }

    public void signUp(User user) {
        Optional<User> userExists = userRepository.findUserByEmail(user.getEmail());
        if (userExists.isPresent()){
            if(!userExists.get().isAccountVerified()){

                sendActivationMessage(userExists.get());

                throw new EmailNotConfirmedException("Email already exist. Please confirm it");
            }
            throw new UserAlreadyExistAuthenticationException("User already exist");
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        userRepository.save(user);
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

    private void sendActivationMessage(User user) {
        if (!user.getEmail().isBlank()) {
            String token = buildConfirmationToken(user);
            AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
            emailContext.init(user);
            emailContext.buildVerificationUrl(baseUrl, token);
            try {
                emailService.sendMail(emailContext);
            } catch (MessagingException e) {
                throw new EmailMessagingException(String.format("Message to %s was not send", user.getEmail()));
            }
        }
        log.info("Activation message was send to user email: " + user.getEmail());
    }

    @Transactional
    @Override
    public ResponseEntity<?> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new ConfirmationTokenException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenException("Token expired");
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

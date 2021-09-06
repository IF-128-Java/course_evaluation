package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.PasswordRestoreDto;
import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.ConfirmationTokenException;
import ita.softserve.course_evaluation.exception.EmailMessagingException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.ConfirmationTokenService;
import ita.softserve.course_evaluation.service.PasswordRecoveryService;
import ita.softserve.course_evaluation.service.mail.EmailService;
import ita.softserve.course_evaluation.service.mail.context.ForgotPasswordEmailContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    @Value("${site.base.url.https}")
    private String baseUrl;

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordRecoveryServiceImpl(ConfirmationTokenService confirmationTokenService, EmailService emailService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void forgottenPassword(String userName) {
        log.info("Password recovery service invoke: " + userName);
        Optional<User> userExist = userRepository.findUserByEmail(userName);
        if (userExist.isPresent()) {
            sendPasswordResetMessage(userExist.get());
        } else {
            throw new UsernameNotFoundException("User with given email not exist");
        }
    }

    private void sendPasswordResetMessage(User user) {
        if (!user.getEmail().isBlank()) {
            String token = buildConfirmationToken(user);
            ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
            emailContext.buildVerificationUrl(baseUrl, token);
            emailContext.init(user);
            try {
                emailService.sendMail(emailContext);
            } catch (MessagingException e) {
                throw new EmailMessagingException(String.format("Message to %s was not send", user.getEmail()));
            }
        }
        log.info("Reset Password message was send to email: " + user.getEmail());
    }

    private String buildConfirmationToken(User user) {
        String mailToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                mailToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return mailToken;
    }

    @Override
    @Transactional
    public void updatePassword(PasswordRestoreDto restoreDto) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.getToken(restoreDto.getToken());
        if (confirmationToken.isEmpty() ||
                !StringUtils.equals(restoreDto.getToken(), confirmationToken.get().getToken()) ||
                confirmationToken.get().getExpiredAt().isBefore(LocalDateTime.now()) ||
                confirmationToken.get().getConfirmedAt() != null) {
            throw new ConfirmationTokenException("Token is invalid/expired");
        }
        Optional<User> userExist = userRepository.findUserById(confirmationToken.get().getAppUser().getId());
        if (userExist.isPresent()) {
            User user = userExist.get();
            user.setPassword(passwordEncoder.encode(restoreDto.getPassword()));
            userRepository.save(user);
        }
        confirmationTokenService.setConfirmedAt(confirmationToken.get().getToken());
    }

}

package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.ConfirmationTokenRepository;
import ita.softserve.course_evaluation.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public void updateConfirmationToken(User user, ConfirmationToken confirmationToken) {
        ConfirmationToken existingToken = confirmationTokenRepository.findByAppUser(user);
        if(existingToken != null){
            existingToken.setToken(confirmationToken.getToken());
            existingToken.setCreatedAt(confirmationToken.getCreatedAt());
            existingToken.setExpiredAt(confirmationToken.getExpiredAt());
            confirmationTokenRepository.save(existingToken);
        }else {
            confirmationTokenRepository.save(confirmationToken);
        }
    }
}

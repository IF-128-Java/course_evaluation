package ita.softserve.course_evaluation.registration.token;

import ita.softserve.course_evaluation.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void deleteToken(User user) {
        confirmationTokenRepository.deleteByAppUser(user);
    };

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

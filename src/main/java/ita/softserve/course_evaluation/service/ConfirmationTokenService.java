package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.ConfirmationToken;
import ita.softserve.course_evaluation.entity.User;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    void setConfirmedAt(String token);

    Optional<ConfirmationToken> getToken(String token);

    void updateConfirmationToken(User user, ConfirmationToken confirmationToken);
}

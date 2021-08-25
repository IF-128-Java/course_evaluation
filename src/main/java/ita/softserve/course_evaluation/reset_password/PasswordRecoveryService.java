package ita.softserve.course_evaluation.reset_password;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface PasswordRecoveryService {

    void forgottenPassword(String userName);

    void updatePassword(PasswordRestoreDto passwordRestoreDto);

}

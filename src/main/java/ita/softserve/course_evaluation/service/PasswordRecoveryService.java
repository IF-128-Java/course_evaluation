package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.PasswordRestoreDto;

public interface PasswordRecoveryService {

    void forgottenPassword(String userName);

    void updatePassword(PasswordRestoreDto passwordRestoreDto);

}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {

    ResponseEntity<?> register(SimpleUserDto request);

    @Transactional
    ResponseEntity<?> confirmToken(String token);

    void enableUserEmail(String email);
}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
	ResponseEntity<?> getLoginCredentials(AuthenticateRequestDto request);
}

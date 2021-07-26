package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
	ResponseEntity<?> getLoginCredentials(AuthenticateRequestDto request);
	
	ResponseEntity<?> getRegistrationCredentials(SimpleUserDto request);
}

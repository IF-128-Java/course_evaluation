package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/auth")
public class LoginController {
	
	private final AuthService authService;
	
	public LoginController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticateRequestDto request) {
		return authService.getLoginCredentials(request);
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		securityContextLogoutHandler.logout(request, response, null);
	}
	
	@PostMapping("/reg")
	public ResponseEntity<?> registration(@RequestBody SimpleUserDto request) {
		return authService.getRegistrationCredentials(request);
	}
}

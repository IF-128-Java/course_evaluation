package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.service.impl.RegistrationServiceImpl;
import ita.softserve.course_evaluation.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "Login service REST API")
@RestController
@RequestMapping("api/v1/auth")
public class LoginController {
	
	private final AuthService authService;
	@Autowired
	private RegistrationServiceImpl registrationService;
	
	public LoginController(AuthService authService) {
		this.authService = authService;
	}

	@ApiOperation(value = "Authenticate user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
			@ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
			@ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
	})
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticateRequestDto request) {
		return authService.getLoginCredentials(request);
	}

	@ApiOperation(value = "Logout")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
	})
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		securityContextLogoutHandler.logout(request, response, null);
	}

	@ApiOperation(value = "Registration")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
	})
	@PostMapping("/reg")
	public ResponseEntity<?> registration(@Valid @RequestBody SimpleUserDto request) {
		return registrationService.register(request);
	}

	@ApiOperation(value = "Email verification")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = HttpStatuses.OK),
			@ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
	})
	@GetMapping(path = "/confirm")
	public ResponseEntity<?> confirm(@RequestParam("token") String token) {
		return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
	}
}

package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
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

@Api(tags = "Login service REST API")
@RestController
@RequestMapping("api/v1/auth")
public class LoginController {
	
	private final AuthService authService;
	
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
	public ResponseEntity<?> registration(@RequestBody SimpleUserDto request) {
		return authService.getRegistrationCredentials(request);
	}
}

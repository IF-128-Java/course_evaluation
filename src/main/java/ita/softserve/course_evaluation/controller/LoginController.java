package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.PasswordRestoreDto;
import ita.softserve.course_evaluation.service.PasswordRecoveryService;
import ita.softserve.course_evaluation.service.RegistrationService;
import ita.softserve.course_evaluation.service.AuthService;
import ita.softserve.course_evaluation.two_factor_verif.SignUpResponse2fa;
import ita.softserve.course_evaluation.two_factor_verif.TotpManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Api(tags = "Login service REST API")
@RestController
@RequestMapping("api/v1/auth")
public class LoginController {

    private final TotpManager totpManager;

    private final AuthService authService;
    private final RegistrationService registrationService;
    private final PasswordRecoveryService passwordRecoveryService;

    public LoginController(TotpManager totpManager, AuthService authService, RegistrationService registrationService, PasswordRecoveryService passwordRecoveryService) {
        this.totpManager = totpManager;
        this.authService = authService;
        this.registrationService = registrationService;
        this.passwordRecoveryService = passwordRecoveryService;
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

        ResponseEntity<?> response = registrationService.register(request);

        if (request.isActive_2fa()) {
            String qrCodeImage = totpManager.getUriForImage(request.getEmail());
            return new ResponseEntity<>(new SignUpResponse2fa(true, qrCodeImage), HttpStatus.OK);
        }
        return response;
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

    @ApiOperation("Sending email for restore password.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = "USER_NOT_FOUND_BY_EMAIL")
    })
    @GetMapping("/restorePassword")
    public ResponseEntity<?> restore(@Valid @RequestParam @Email(message = "INVALID RESTORE EMAIL ADDRESS")
                                             String email) {
        passwordRecoveryService.forgottenPassword(email);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Password reset with confirmation token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
    })
    @PostMapping(path = "/changePassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordRestoreDto restoreDto) {
        passwordRecoveryService.updatePassword(restoreDto);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ApiOperation(value = "Verify Two-Factor authentication code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
    })
    @PostMapping("/verify")
    @PreAuthorize("hasRole('ROLE_PRE_VERIFICATION')")
    public ResponseEntity<?> verifyCode(@NotEmpty @RequestParam String code, @RequestParam String email) {
        return totpManager.verifyCode(code, email);
    }

}

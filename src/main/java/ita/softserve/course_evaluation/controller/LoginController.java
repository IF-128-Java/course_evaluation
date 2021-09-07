package ita.softserve.course_evaluation.controller;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.PasswordRestoreDto;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.CustomQrGenerationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.security.jwt.JwtTokenProvider;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import ita.softserve.course_evaluation.service.PasswordRecoveryService;
import ita.softserve.course_evaluation.service.RegistrationService;
import ita.softserve.course_evaluation.service.AuthService;
import ita.softserve.course_evaluation.service.UserService;
import ita.softserve.course_evaluation.two_factor_verif.SignUpResponse2fa;
import liquibase.pro.packaged.S;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Api(tags = "Login service REST API")
@RestController
@RequestMapping("api/v1/auth")
public class LoginController {

    @Autowired
    private QrGenerator qrGenerator;
    @Autowired
    private QrDataFactory qrDataFactory;
    @Autowired
    private CodeVerifier codeVerifier;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    private final AuthService authService;
    private final RegistrationService registrationService;
    private final PasswordRecoveryService passwordRecoveryService;

    public LoginController(AuthService authService, RegistrationService registrationService, PasswordRecoveryService passwordRecoveryService) {
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

        System.out.println(request.isActive_2fa());
        ResponseEntity<?> response = registrationService.register(request);

        if (request.isActive_2fa()) {
            Optional<User> existUser = userRepository.findUserByEmail(request.getEmail());
            QrData data = qrDataFactory.newBuilder().label(request.getEmail()).secret(existUser.get().getSecret()).issuer("Course Evaluation").build();
            try {
                String qrCodeImage = getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
                return new ResponseEntity<>(new SignUpResponse2fa(true, qrCodeImage), HttpStatus.OK);
            } catch (QrGenerationException e) {
                throw new CustomQrGenerationException("Qr not generated");
            }
        }
        return response;
    }


    @PostMapping("/verify")
    @PreAuthorize("hasRole('ROLE_PRE_VERIFICATION')")
    public ResponseEntity<?> verifyCode(@NotEmpty @RequestBody String code, LocalUser user) {
        if (!codeVerifier.isValidCode(user.getUser().getSecret(), code)) {
            return new ResponseEntity<>("Invalid Code!", HttpStatus.BAD_REQUEST);
        }
        User user1 = user.getUser();
        String [] roles = user1.getRoles().stream().map(Enum::name).toArray(String[]::new);
        String jwt = tokenProvider.createToken(user1.getEmail(), user1.getId(), roles, true);
        Map<Object, Object> response = new HashMap<>();
        response.put("token", jwt);
        return ResponseEntity.ok(response);
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

}

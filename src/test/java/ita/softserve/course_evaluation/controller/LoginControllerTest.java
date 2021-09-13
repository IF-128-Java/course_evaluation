package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.exception.handler.GlobalControllerExceptionHandler;
import ita.softserve.course_evaluation.service.AuthService;
import ita.softserve.course_evaluation.service.PasswordRecoveryService;
import ita.softserve.course_evaluation.service.RegistrationService;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {LoginController.class, SpringSecurityTestConfiguration.class, GlobalControllerExceptionHandler.class})
@Disabled("Not implemented yet")
class LoginControllerTest {
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean private TotpManager totpManager;
    @MockBean private AuthService authService;
    @MockBean private RegistrationService registrationService;
    @MockBean private PasswordRecoveryService passwordRecoveryService;

    @BeforeEach
    void beforeEach(){
        mapper = new ObjectMapper();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(totpManager, authService, registrationService, passwordRecoveryService);
    }

    @Test
    void testAuthenticate() {
        AuthenticateRequestDto authenticateRequestDto = new AuthenticateRequestDto();
        authenticateRequestDto.setEmail("admin@mail.com");
        authenticateRequestDto.setPassword("password");

        Map<Object, Object> response = new HashMap<>();
        response.put("token", "test token");
        ResponseEntity<?> responseEntity = ResponseEntity.ok(response);
//        when(authService.getLoginCredentials(Mockito.any(AuthenticateRequestDto.class))).thenReturn(responseEntity);
    }

    @Test
    void testLogout() {

    }

    @Test
    void testRegistration() {

    }

    @Test
    void testConfirm() {
    }

    @Test
    void testRestore() {
    }

    @Test
    void testResetPassword() {
    }

    @Test
    void testVerifyCode() {
    }
}
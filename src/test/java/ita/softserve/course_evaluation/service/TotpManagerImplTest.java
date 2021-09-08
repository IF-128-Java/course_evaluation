package ita.softserve.course_evaluation.service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.jwt.JwtTokenProvider;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Mykhailo Fedenko
 */
@ExtendWith(MockitoExtension.class)
class TotpManagerImplTest {

    private User userMike;

    @Mock
    private QrGenerator qrGenerator;
    @Mock
    private QrDataFactory qrDataFactory;
    @Mock
    private CodeVerifier codeVerifier;
    @Mock
    private SecretGenerator secretGenerator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private TotpManagerImpl totpManager;

    @BeforeEach
    void beforeEach() {
        userMike = new User();
        userMike.setId(1L);
        userMike.setFirstName("Mike");
        userMike.setLastName("Green");
        userMike.setEmail("email@mail.com");
        userMike.setPassword("password");
        userMike.setAccountVerified(true);
        userMike.setSecret("secret");
        userMike.setRoles(Collections.singleton(Role.ROLE_STUDENT));
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(secretGenerator, tokenProvider, userRepository, codeVerifier, qrGenerator, qrDataFactory);
    }

    @Test
    void testGenerateSecret() {

        when(secretGenerator.generate()).thenReturn("secret code");

        String actual = totpManager.generateSecret();
        assertEquals("secret code", actual);
        verify(secretGenerator, times(1)).generate();
        verifyNoMoreInteractions(secretGenerator, tokenProvider, userRepository, codeVerifier, qrGenerator, qrDataFactory);

    }

    @Test
    void testGetUriForImageWhenUserNotExists() {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> totpManager.getUriForImage("test@mail.com"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
    }

    @Test
    void testGetUriForImageWhenExists() throws QrGenerationException {
        QrData.Builder qrData = new QrData.Builder();
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userMike));
        when(qrDataFactory.newBuilder()).thenReturn(qrData);
        when(qrGenerator.generate(Mockito.any(QrData.class))).thenReturn(new byte[]{1, 2, 3});
        when(qrGenerator.getImageMimeType()).thenReturn("test");

        String uriForImage = totpManager.getUriForImage(userMike.getEmail());
        assertNotNull(uriForImage);

        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
        verify(qrDataFactory, times(1)).newBuilder();
    }

    @Test
    void testVerifyWhenInvalidCode() {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userMike));
        when(codeVerifier.isValidCode(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        ResponseEntity<?> actual = totpManager.verifyCode("code", userMike.getEmail());
        assertEquals("Invalid Code!", actual.getBody());
        verify(codeVerifier, times(1)).isValidCode(Mockito.anyString(), Mockito.anyString());
        verify(userRepository).findUserByEmail(userMike.getEmail());
    }

    @Test
    void testVerifyWhenValidCode() {
        Map<String, String> expected = new HashMap<>();
        expected.put("token", "Jwt Token created");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userMike));
        when(codeVerifier.isValidCode(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(tokenProvider.createToken(Mockito.anyString(), Mockito.anyLong(), Mockito.any(), Mockito.anyBoolean())).thenReturn("Jwt Token created");

        ResponseEntity<?> actual = totpManager.verifyCode("code", userMike.getEmail());
        assertEquals(expected, actual.getBody());
        verify(codeVerifier, times(1)).isValidCode(Mockito.anyString(), Mockito.anyString());
        verify(userRepository).findUserByEmail(userMike.getEmail());
        verify(tokenProvider).createToken(Mockito.anyString(), Mockito.anyLong(), Mockito.any(), Mockito.anyBoolean());
    }

    @Test
    void testSwitch2faStatus() {

        doNothing().when(userRepository).updateStatus2FA(Mockito.anyString(), Mockito.anyBoolean());

        totpManager.switch2faStatus(userMike.getEmail(), true);

        verify(userRepository, times(1)).updateStatus2FA(Mockito.anyString(), Mockito.anyBoolean());

    }
}
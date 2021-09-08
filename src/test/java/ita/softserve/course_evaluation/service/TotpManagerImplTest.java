package ita.softserve.course_evaluation.service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.jwt.JwtTokenProvider;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mykhailo Fedenko on 08.09.2021
 */
@ExtendWith(MockitoExtension.class)
class TotpManagerImplTest {

    private User userMike;

    @Mock private QrGenerator qrGenerator;
    @Mock private QrDataFactory qrDataFactory;
    @Mock private CodeVerifier codeVerifier;
    @Mock private SecretGenerator secretGenerator;
    @Mock private UserRepository userRepository;
    @Mock private JwtTokenProvider tokenProvider;

    @InjectMocks private TotpManagerImpl totpManager;

    @BeforeEach
    void beforeEach(){
        userMike = new User();
        userMike.setId(1L);
        userMike.setFirstName("Mike");
        userMike.setLastName("Green");
        userMike.setEmail("email@mail.com");
        userMike.setPassword("password");
        userMike.setAccountVerified(true);
        userMike.setSecret("secret");
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
        Exception exception = assertThrows(UsernameNotFoundException.class, ()-> totpManager.getUriForImage("test@mail.com"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
        verifyNoMoreInteractions(secretGenerator, tokenProvider, userRepository, codeVerifier, qrGenerator, qrDataFactory);
    }

    @Test
    @Disabled("Not implemented")
    void testGetUriForImageWhenExists() throws QrGenerationException {
        QrData.Builder qrData = new QrData.Builder();
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(userMike));
        when(qrDataFactory.newBuilder()).thenReturn(qrData);
        when(qrGenerator.generate(any(QrData.class))).thenReturn(new byte[]{1,2,3});
        when(qrGenerator.getImageMimeType()).thenReturn("test");

        String uriForImage = totpManager.getUriForImage(userMike.getEmail());
        assertNotNull(uriForImage);

        verify(userRepository, times(1)).findUserByEmail(Mockito.anyString());
        verify(qrDataFactory, times(1)).newBuilder();
        verify(qrGenerator, times(1)).getImageMimeType();
        verify(qrGenerator, times(1)).generate(qrData.build());
        verifyNoMoreInteractions(secretGenerator, tokenProvider, userRepository, codeVerifier, qrGenerator, qrDataFactory);
    }

    @Test
    @Disabled("Not implemented")
    void testVerifyCode() {

    }

    @Test
    @Disabled("Not implemented")
    void testSwitch2faStatus() {
    }
}
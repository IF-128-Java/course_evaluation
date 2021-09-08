package ita.softserve.course_evaluation.two_factor_verif;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.CustomQrGenerationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * @author Mykhailo Fedenko on 06.09.2021
 */
@Service
@Slf4j
public class TotpManagerImpl implements TotpManager{

    private final QrGenerator qrGenerator;
    private final QrDataFactory qrDataFactory;
    private final CodeVerifier codeVerifier;
    private final SecretGenerator secretGenerator;

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public TotpManagerImpl(QrGenerator qrGenerator, QrDataFactory qrDataFactory, CodeVerifier codeVerifier, SecretGenerator secretGenerator, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.qrGenerator = qrGenerator;
        this.qrDataFactory = qrDataFactory;
        this.codeVerifier = codeVerifier;
        this.secretGenerator = secretGenerator;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public String generateSecret(){
        return secretGenerator.generate();
    }

    public String getUriForImage(String email) {
        Optional<User> existUser = userRepository.findUserByEmail(email);
        User user = existUser.orElseThrow(()-> new UsernameNotFoundException("User not found"));
        QrData qrData = qrDataFactory.newBuilder()
                .label(user.getEmail())
                .issuer("Course Evaluation")
                .secret(user.getSecret())
                .build();
        try {
            return getDataUriForImage(qrGenerator.generate(qrData), qrGenerator.getImageMimeType());
        } catch (QrGenerationException e) {
            throw new CustomQrGenerationException("Unable to generate QR code!");
        }
    }

    public ResponseEntity<?> verifyCode(String code, String email) {

        Optional<User> existUser = userRepository.findUserByEmail(email);
        User user = existUser.orElseThrow();
        if (!codeVerifier.isValidCode(user.getSecret(), code)) {
            return new ResponseEntity<>("Invalid Code!", HttpStatus.BAD_REQUEST);
        }

        String [] roles = user.getRoles().stream().map(Enum::name).toArray(String[]::new);
        String jwt = tokenProvider.createToken(user.getEmail(), user.getId(), roles, true);
        Map<Object, Object> response = new HashMap<>();
        response.put("token", jwt);
        return ResponseEntity.ok(response);

    }

    public void switch2faStatus(String email, boolean status){
        userRepository.updateStatus2FA(email, status);
    }
}

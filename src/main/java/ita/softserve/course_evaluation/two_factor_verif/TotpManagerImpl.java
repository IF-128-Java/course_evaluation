package ita.softserve.course_evaluation.two_factor_verif;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.CustomQrGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TotpManagerImpl(QrGenerator qrGenerator, QrDataFactory qrDataFactory, CodeVerifier codeVerifier, SecretGenerator secretGenerator) {
        this.qrGenerator = qrGenerator;
        this.qrDataFactory = qrDataFactory;
        this.codeVerifier = codeVerifier;
        this.secretGenerator = secretGenerator;
    }

    public String generateSecret(){
        return secretGenerator.generate();
    }

    public String getUriForImage(String secret, User user) {
        QrData qrData = qrDataFactory.newBuilder()
                .label(user.getEmail()).issuer("Course Evaluation")
                .secret(user.getSecret())
                .build();
        try {
            return getDataUriForImage(qrGenerator.generate(qrData), qrGenerator.getImageMimeType());
        } catch (QrGenerationException e) {
            throw new CustomQrGenerationException("Unable to generate QR code!");
        }
    }

    public boolean verifyCode(String code, String secret) {
        return codeVerifier.isValidCode(code, secret);
    }
}

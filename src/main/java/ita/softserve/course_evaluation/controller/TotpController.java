package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.two_factor_verif.SignUpResponse2fa;
import ita.softserve.course_evaluation.two_factor_verif.TotpManager;
import ita.softserve.course_evaluation.two_factor_verif.TotpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Mykhailo Fedenko on 07.09.2021
 */
@RestController
@Slf4j
@RequestMapping("api/v1/totp")
public class TotpController {

    private final TotpManager totpManager;
    private final UserRepository userRepository;

    public TotpController(TotpManager totpManager, UserRepository userRepository) {
        this.totpManager = totpManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/change2faStatus")
    public ResponseEntity<?> switch2FA(@RequestBody TotpRequest totpRequest){
        totpManager.switch2faStatus(totpRequest.getEmail(), totpRequest.isActive2fa());
        if (totpRequest.isActive2fa()) {

            String qrCodeImage = totpManager.getUriForImage(totpRequest.getEmail());
            return new ResponseEntity<>(new SignUpResponse2fa(true, qrCodeImage), HttpStatus.OK);

        }

        return ResponseEntity.ok("2FA turned off");
    }
}

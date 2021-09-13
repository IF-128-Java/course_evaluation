package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.service.two_factor_verif.SignUpResponse2fa;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpManager;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mykhailo Fedenko
 */
@RestController
@Slf4j
@RequestMapping("api/v1/totp")
public class TotpController {

    private final TotpManager totpManager;

    public TotpController(TotpManager totpManager) {
        this.totpManager = totpManager;
    }

    @PostMapping("/change2faStatus")
    public ResponseEntity<?> updateStatus2FA(@RequestBody TotpRequestDto totpRequest){
        totpManager.switch2faStatus(totpRequest.getEmail(), totpRequest.isActive2fa());
        if (totpRequest.isActive2fa()) {

            String qrCodeImage = totpManager.getUriForImage(totpRequest.getEmail());
            return new ResponseEntity<>(new SignUpResponse2fa(true, qrCodeImage), HttpStatus.OK);
        }

        return ResponseEntity.ok("2FA turned off");
    }
}

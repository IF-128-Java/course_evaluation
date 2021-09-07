package ita.softserve.course_evaluation.two_factor_verif;

import org.springframework.http.ResponseEntity;

public interface TotpManager {

  String generateSecret();

  String getUriForImage(String email);

  ResponseEntity<?> verifyCode(String code, String secret);

  void switch2faStatus(String email, boolean status);
}
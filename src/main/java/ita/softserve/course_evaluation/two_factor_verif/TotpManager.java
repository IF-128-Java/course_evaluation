package ita.softserve.course_evaluation.two_factor_verif;

import ita.softserve.course_evaluation.entity.User;

public interface TotpManager {

  String generateSecret();

  String getUriForImage(String secret, User user);

  boolean verifyCode(String code, String secret);

}
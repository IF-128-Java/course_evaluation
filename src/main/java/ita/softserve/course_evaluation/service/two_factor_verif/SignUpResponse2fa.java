package ita.softserve.course_evaluation.service.two_factor_verif;

import lombok.Value;

@Value
public class SignUpResponse2fa {
	boolean using2FA;
	String qrCodeImage;
}
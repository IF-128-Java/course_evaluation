package ita.softserve.course_evaluation.two_factor_verif;

import lombok.Value;

@Value
public class SignUpResponse2fa {
	private boolean using2FA;
	private String qrCodeImage;
}
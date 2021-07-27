package ita.softserve.course_evaluation.dto;

import lombok.Data;

@Data
public class AuthenticateRequestDto {
	private String email;
	private String password;
}

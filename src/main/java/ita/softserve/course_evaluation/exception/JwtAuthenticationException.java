package ita.softserve.course_evaluation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;


public class JwtAuthenticationException extends AuthenticationException {
	private HttpStatus status;
	
	public JwtAuthenticationException(String msg, HttpStatus status) {
		super(msg);
		this.status = status;
	}
	
	public JwtAuthenticationException(String msg) {
		super(msg);
	}
	
	public HttpStatus getStatus() {
		return status;
	}
}

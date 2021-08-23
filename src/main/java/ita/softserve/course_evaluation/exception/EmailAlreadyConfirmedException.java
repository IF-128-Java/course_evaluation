package ita.softserve.course_evaluation.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailAlreadyConfirmedException extends AuthenticationException {
    public EmailAlreadyConfirmedException(String msg) {
        super(msg);
    }
}

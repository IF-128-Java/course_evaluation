package ita.softserve.course_evaluation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FeedbackAlreadyExistsException extends RuntimeException{

    public FeedbackAlreadyExistsException() { super(); }

    public FeedbackAlreadyExistsException(String message) {
        super(message);
    }

}

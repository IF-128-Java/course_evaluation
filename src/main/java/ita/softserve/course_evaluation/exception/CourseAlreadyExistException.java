package ita.softserve.course_evaluation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseAlreadyExistException extends RuntimeException {

    public CourseAlreadyExistException() {
        super();
    }

    public CourseAlreadyExistException(String message) {
        super(message);
    }


}

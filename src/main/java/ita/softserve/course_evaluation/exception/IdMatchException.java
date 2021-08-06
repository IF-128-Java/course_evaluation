package ita.softserve.course_evaluation.exception;

public class IdMatchException extends RuntimeException{

    public IdMatchException(String message) {
        super(message);
    }

    public IdMatchException() {
    }
}

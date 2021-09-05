package ita.softserve.course_evaluation.exception;

/**
 * @author Mykhailo Fedenko on 02.09.2021
 */
public class EmailNotValidException extends RuntimeException {

    public EmailNotValidException(String msg) {
        super(msg);
    }
}

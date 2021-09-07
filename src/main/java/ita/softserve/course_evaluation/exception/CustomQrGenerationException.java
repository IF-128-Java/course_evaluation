package ita.softserve.course_evaluation.exception;

/**
 * @author Mykhailo Fedenko on 06.09.2021
 */
public class CustomQrGenerationException extends RuntimeException {
    public CustomQrGenerationException(String msg) {
        super(msg);
    }
}

package ita.softserve.course_evaluation.exception;

public class FileProcessingException extends RuntimeException{
    public FileProcessingException() {
    }

    public FileProcessingException(String message) {
        super(message);
    }
}

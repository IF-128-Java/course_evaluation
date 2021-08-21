package ita.softserve.course_evaluation.exception;

public class FileHasNoExtensionException extends RuntimeException{
    public FileHasNoExtensionException() {
    }

    public FileHasNoExtensionException(String message) {
        super(message);
    }
}

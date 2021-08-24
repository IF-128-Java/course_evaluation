package ita.softserve.course_evaluation.exception;

public class EmailNotConfirmedException extends RuntimeException{

    public EmailNotConfirmedException (String msg){
        super(msg);
    }
}

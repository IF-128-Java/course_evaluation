package ita.softserve.course_evaluation.service;

public interface MailSender {
    void send(String to, String subject, String message);
}

package ita.softserve.course_evaluation.service.mail;


import ita.softserve.course_evaluation.service.mail.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws MessagingException;

}

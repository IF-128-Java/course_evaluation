package ita.softserve.course_evaluation.service.mail;


import ita.softserve.course_evaluation.dto.MailDto;
import ita.softserve.course_evaluation.service.mail.context.AbstractEmailContext;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws MessagingException;

    void sendSimpleEmail (MailDto maildto) throws AddressException;

}

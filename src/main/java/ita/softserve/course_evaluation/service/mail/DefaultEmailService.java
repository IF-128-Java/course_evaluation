package ita.softserve.course_evaluation.service.mail;

import ita.softserve.course_evaluation.dto.MailDto;
import ita.softserve.course_evaluation.service.mail.context.AbstractEmailContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@EnableAsync
public class DefaultEmailService implements EmailService {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public DefaultEmailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendMail(AbstractEmailContext email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(email.getContext());
        String emailContent = templateEngine.process(email.getTemplateLocation(), context);

        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setFrom(email.getFrom());
        mimeMessageHelper.setText(emailContent, true);
        emailSender.send(message);

    }

    @Override
    public void sendSimpleEmail (MailDto maildto) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(maildto.getTo().split(","));
        simpleMailMessage.setSubject(maildto.getSubject());
        simpleMailMessage.setText(maildto.getMessage());

        emailSender.send(simpleMailMessage);
    }
}

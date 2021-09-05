package ita.softserve.course_evaluation.service.mail.context;

import ita.softserve.course_evaluation.entity.User;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext{

    @Override
    public <T> void init(T context) {
        User user = (User) context;
        put("userName", user.getFirstName() + " " + user.getLastName());
        setTemplateLocation("emails/email-verification");
        setSubject("Complete your registration");
        setFrom("noreply.courseevaluation@gmail.com");
        setTo(user.getEmail());
    }


    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/confirm").queryParam("token", token).toUriString();
        put("verificationURL", url);
        put("baseUrl", baseURL);
        put("token", token);
    }
}

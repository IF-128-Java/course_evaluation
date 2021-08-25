package ita.softserve.course_evaluation.service.mail.context;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.entity.User;
import org.springframework.web.util.UriComponentsBuilder;

public class ForgotPasswordEmailContext extends AbstractEmailContext{

    @Override
    public <T> void init(T context){

        User customer = (User) context;
        put("userName", customer.getFirstName() +" "+ customer.getLastName());
        setTemplateLocation("emails/forgot-password");
        setSubject("Password Recovery");
        setFrom("noreply.courseevaluation@gmail.com");
        setTo(customer.getEmail());
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/changePassword").queryParam("token", token).toUriString();
        put("resetPasswordURL", url);
        put("token", token);
        put("baseUrl", baseURL);
    }



}

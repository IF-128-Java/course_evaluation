package ita.softserve.course_evaluation.security.oauth2;

import ita.softserve.course_evaluation.exception.OAuth2AuthenticationProcessingException;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOidUserService extends OidcUserService {

    @Autowired
    private UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser OidcUser = super.loadUser(userRequest);
//        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            Map<String, Object> attributes = new HashMap<>(OidcUser.getAttributes());

            String provider = userRequest.getClientRegistration().getRegistrationId();
            return OidcUser;
//            return userService.processUserRegistration(provider, attributes, null, null);
        } catch (AuthenticationException exception) {
            throw exception;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new OAuth2AuthenticationProcessingException(exception.getMessage(), exception.getCause());
        }
    }

}

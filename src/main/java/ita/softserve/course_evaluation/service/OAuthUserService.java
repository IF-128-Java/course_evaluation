package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SignUpRequest;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;

public interface OAuthUserService {

    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

    User registerNewUser(final SignUpRequest signUpRequest);

}

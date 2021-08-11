package ita.softserve.course_evaluation.security.oauth2;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.OAuth2AuthenticationProcessingException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfo;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfoFactory;
import ita.softserve.course_evaluation.security.oauth2.users.SocialProvider;
import ita.softserve.course_evaluation.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
            String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
            return userService.processUserRegistration(provider, attributes, null, null);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            // Throwing an instance of AuthenticationException will trigger the
            // OAuth2AuthenticationFailureHandler
            throw new OAuth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
        }
    }
}
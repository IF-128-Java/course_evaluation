package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.OAuth2AuthenticationProcessingException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfo;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfoFactory;
import ita.softserve.course_evaluation.service.OAuthUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OAuth2UserServiceImpl implements OAuthUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        SimpleUserDto userDetails = toUserRegistrationObject(oAuth2UserInfo);

        User user = userRepository.findUserByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        if (user == null) {
            user = registerNewUser(userDetails);
        }

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getName().split(" ")[0]);
        existingUser.setLastName(oAuth2UserInfo.getName().split(" ")[1]);

        return userRepository.save(existingUser);
    }

    private SimpleUserDto toUserRegistrationObject(OAuth2UserInfo oAuth2UserInfo) {
        return SimpleUserDto.builder()
                .firstName(oAuth2UserInfo.getName().split(" ")[0])
                .lastName(oAuth2UserInfo.getName().split(" ")[1])
                .email(oAuth2UserInfo.getEmail())
                .password(passwordEncoder.encode("changeit")).build();
    }

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SimpleUserDto signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getEmail() != null && userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        }
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRoles(Stream.of(Role.ROLE_STUDENT).collect(Collectors.toSet()));
        user.setPassword(signUpRequest.getPassword());

        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }
}

package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.SignUpRequest;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.OAuth2AuthenticationProcessingException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfo;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfoFactory;
import ita.softserve.course_evaluation.service.OAuthUserService;
import ita.softserve.course_evaluation.util.GeneralUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OAuthUserServiceImpl implements OAuthUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuthUserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);

        User user = userRepository.findUserByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        if (user != null) {
//			if (!OAuthUserDtoMapper.toDto(user).getProvider().equals(registrationId) && !OAuthUserDtoMapper.toDto(user).getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
//				throw new OAuth2AuthenticationProcessingException(
//						"Looks like you're signed up with " + OAuthUserDtoMapper.toDto(user).getProvider() + " account. Please use your " + OAuthUserDtoMapper.toDto(user).getProvider() + " account to login.");
//			}
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userDetails);
        }

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getName().split(" ")[0]);
        existingUser.setLastName(oAuth2UserInfo.getName().split(" ")[1]);

        return userRepository.save(existingUser);
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .displayName(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .socialProvider(GeneralUtils.toSocialProvider(registrationId))
                .password(passwordEncoder.encode("changeit")).build();
    }

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        }
        User user = new User();
        user.setId(signUpRequest.getUserID());
        user.setFirstName(signUpRequest.getDisplayName().split(" ")[0]);
        user.setLastName(signUpRequest.getDisplayName().split(" ")[1]);
        user.setEmail(signUpRequest.getEmail());
        user.setRoles(Stream.of(Role.ROLE_STUDENT).collect(Collectors.toSet()));
        user.setPassword(signUpRequest.getPassword());

        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }
}

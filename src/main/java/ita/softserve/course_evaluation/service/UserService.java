package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SignUpRequest;
import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface UserService {

	UserDto readById(long id);

	List<UserDto> readByFirstName(String firstName);

	void updateUser(UpdateUserDto dto, Long userId);

	void updatePassword(UpdatePasswordDto updatePasswordDto, Long userId);

	public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

	User registerNewUser(final SignUpRequest signUpRequest);

}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Map;

public interface UserService {

	UserDto readById(long id);

	List<UserDto> readByFirstName(String firstName);

	void updateUser(UpdateUserDto dto, Long userId);

	void updatePassword(UpdatePasswordDto updatePasswordDto, Long userId);

//	User registerNewUser(final SignUpRequest signUpRequest)

//	LocalUser processUserRegistration(String provider, Map<String, Object> attributes, Object o, Object o1);
}

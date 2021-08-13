package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.*;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.exception.OAuth2AuthenticationProcessingException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfo;
import ita.softserve.course_evaluation.security.oauth2.users.OAuth2UserInfoFactory;
import ita.softserve.course_evaluation.security.oauth2.users.SocialProvider;
import ita.softserve.course_evaluation.service.UserService;
import ita.softserve.course_evaluation.util.GeneralUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDto readById(long id ) {
		return UserDtoMapper.toDto(getUserById(id));
	}

	@Override
	public List<UserDto> readByFirstName(String firstName) {
		return UserDtoMapper.toDto(userRepository.findUserByFirstName(firstName));
	}

	@Override
	public void updateUser(UpdateUserDto dto, String email) {
		User daoUser = getUserByEmail(email);

		daoUser.setFirstName(dto.getFirstName());
		daoUser.setLastName(dto.getLastName());

		userRepository.save(daoUser);
	}

	@Override
	public void updatePassword(UpdatePasswordDto dto, String email) {
		User daoUser = getUserByEmail(email);

		if(!passwordEncoder.matches(dto.getOldPassword(), daoUser.getPassword())){
			throw new InvalidOldPasswordException("Old password doesn't match!");
		}

		daoUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		userRepository.save(daoUser);
	}


	private void checkAuthenticatedUser(Long userId){
		if(!((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(userId))
			throw new IdMatchException("Ids don't match!");
	}

	private User getUserById(Long id){
		return userRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException(String.format("User with id: %d not found!", id)));
	}

	private User getUserByEmail(String email){
		return userRepository.findUserByEmail(email).orElseThrow(
				() -> new EntityNotFoundException(String.format("User with email: %s not found!", email)));
	}
}
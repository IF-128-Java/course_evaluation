package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.registration.ActivaUserRepository;
import ita.softserve.course_evaluation.registration.UserActive;
import ita.softserve.course_evaluation.registration.token.ConfirmationToken;
import ita.softserve.course_evaluation.registration.token.ConfirmationTokenService;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	@Autowired
	private ActivaUserRepository activaUserRepository;

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

	private User getUserById(Long id){
		return userRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException(String.format("User with id: %d not found!", id)));
	}

	private User getUserByEmail(String email){
		return userRepository.findUserByEmail(email).orElseThrow(
				() -> new EntityNotFoundException(String.format("User with email: %s not found!", email)));
	}


	@Override
	public String signUp(User user) {
		boolean userExists = userRepository.findUserByEmail(user.getEmail()).isPresent();
		if (userExists){
			throw new UserAlreadyExistAuthenticationException("email already exist");
		}
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);

		userRepository.save(user);

		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				user
		);
		UserActive userActive = new UserActive();
		userActive.setUser(user);

		activaUserRepository.save(userActive);
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		//TODO: SEND EMAIL
		return token;
	}

	public int enableAppUser(String email) {
		return userRepository.enableAppUser(email);
	}

}
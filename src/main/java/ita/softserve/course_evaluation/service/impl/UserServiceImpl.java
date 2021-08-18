package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.exception.UserAlreadyExistAuthenticationException;
import ita.softserve.course_evaluation.registration.ActivaUserRepository;
import ita.softserve.course_evaluation.registration.RegistrationService;
import ita.softserve.course_evaluation.registration.UserActive;
import ita.softserve.course_evaluation.registration.token.ConfirmationToken;
import ita.softserve.course_evaluation.registration.token.ConfirmationTokenService;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.MailSender;
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
	@Autowired
	private  MailSender mailSender;

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
		Optional<User> userExists = userRepository.findUserByEmail(user.getEmail());
		if (userExists.isPresent()){
			UserActive userActive = activaUserRepository.getByUser(userExists.get());
			if(!userActive.isEnabled()){
				String mailToken = UUID.randomUUID().toString();
				ConfirmationToken confirmationToken = new ConfirmationToken(
						mailToken,
						LocalDateTime.now(),
						LocalDateTime.now().plusMinutes(15),
						userExists.get()
				);
				confirmationTokenService.updateConfirmationToken(userExists.get(), confirmationToken);
//				confirmationTokenService.saveConfirmationToken(confirmationToken);
				String address = "http://localhost:8080";
				String message = String.format(
						"Hello, %s! \n" + "Your activation link: %s/api/v1/auth/confirm?token=%s",
						user.getFirstName() + " " + user.getLastName(),
						address,
						mailToken
				);
				mailSender.send(user.getEmail(),"Activation", message);
				throw new UserAlreadyExistAuthenticationException("Email already exist. Please activate it");
			}
			throw new UserAlreadyExistAuthenticationException("email already exist");
			}
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);

		userRepository.save(user);

		String mailToken = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(
				mailToken,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				user
		);
		UserActive userActive2 = new UserActive();
		userActive2.setUser(user);

		activaUserRepository.save(userActive2);
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		return mailToken;
	}

	public int enableAppUser(String email) {
		return userRepository.enableAppUser(email);
	}

}
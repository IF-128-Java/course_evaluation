package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.IdMatchException;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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
		checkAuthenticatedUser(id);

		return UserDtoMapper.toDto(getUserById(id));
	}

	@Override
	public List<UserDto> readByFirstName(String firstName) {
		return UserDtoMapper.toDto(userRepository.findUserByFirstName(firstName));
	}

	@Override
	public void updateUser(UpdateUserDto dto, Long userId) {
		checkAuthenticatedUser(userId);

		User daoUser = getUserById(userId);

		daoUser.setFirstName(dto.getFirstName());
		daoUser.setLastName(dto.getLastName());

		userRepository.save(daoUser);
	}

	@Override
	public void updatePassword(UpdatePasswordDto updatePasswordDto, Long userId) {
		checkAuthenticatedUser(userId);

		User daoUser = getUserById(userId);

		if(!passwordEncoder.matches(updatePasswordDto.getOldPassword(), daoUser.getPassword())){
			throw new InvalidOldPasswordException("Old password doesn't match!");
		}

		daoUser.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
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
}
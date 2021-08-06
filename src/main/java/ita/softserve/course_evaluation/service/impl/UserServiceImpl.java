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

	public List<UserDto>  readAll() {
		return UserDtoMapper.toDto(userRepository.findAll());
	}

	@Override
	public UserDto readById(long id ) {
		checkAuthenticatedUser(id);

		return UserDtoMapper.toDto(userRepository.findUserById(id).orElseThrow(
				() -> new EntityNotFoundException("User with id: " + id + " not found!")
		));
	}

	@Override
	public UserDto   readByFirstName(String firstName) {return UserDtoMapper.toDto(userRepository.findUserByFirstName(firstName).get());}

	@Override
	public  UserDto createUser(UserDto dto) {
		return UserDtoMapper.toDto(userRepository.save(UserDtoMapper.fromDto(dto)));
	}

	@Override
	public void updateUser(UpdateUserDto dto, Long userId) {
		checkAuthenticatedUser(userId);

		User daoUser = userRepository.findUserById(userId).orElseThrow(
				() -> new EntityNotFoundException("User with id: " + userId + " not found!"));

		daoUser.setFirstName(dto.getFirstName());
		daoUser.setLastName(dto.getLastName());

		userRepository.save(daoUser);
	}

	@Override
	public void updatePassword(UpdatePasswordDto updatePasswordDto, Long userId) {
		checkAuthenticatedUser(userId);

		User daoUser = userRepository.findUserById(userId).orElseThrow(
				() -> new EntityNotFoundException("User with id: " + userId + " not found!"));

		if(!passwordEncoder.matches(updatePasswordDto.getOldPassword(), daoUser.getPassword())){
			throw new InvalidOldPasswordException("Old password doesn't match!");
		}

		daoUser.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
		userRepository.save(daoUser);
	}

	@Override
	public void deleteUser(long id){
		User deletingUser = userRepository.getById(id);
		userRepository.delete(deletingUser);
	}

	private void checkAuthenticatedUser(Long userId){
		if(!((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(userId))
			throw new IdMatchException("Ids don't match!");
	}
}
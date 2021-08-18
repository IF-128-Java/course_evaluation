package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
import ita.softserve.course_evaluation.dto.UserProfileDtoResponse;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.UserService;
import ita.softserve.course_evaluation.util.S3Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

	@Value("${aws.users-folder}")
	private String USERS_FOLDER;

	@Value("${aws.bucket-name}")
	private String BUCKET_NAME;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final S3Utils s3Utils;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, S3Utils s3Utils) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.s3Utils = s3Utils;
	}

	@Override
	public UserProfileDtoResponse readById(long id ) {
		User daoUser = getUserById(id);

		return UserProfileDtoResponse.builder()
				.firstName(daoUser.getFirstName())
				.lastName(daoUser.getLastName())
				.email(daoUser.getEmail())
				.profilePicture(
						Objects.isNull(daoUser.getProfilePicturePath()) ? null : s3Utils.downloadFile(daoUser.getProfilePicturePath(), BUCKET_NAME, USERS_FOLDER)
				)
				.build();
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

	@Override
	public void updateUserProfilePicture(MultipartFile image, String email) {
		User daoUser = getUserByEmail(email);

		if(Objects.nonNull(daoUser.getProfilePicturePath()))
			s3Utils.deleteFile(daoUser.getProfilePicturePath(), BUCKET_NAME, USERS_FOLDER);

		daoUser.setProfilePicturePath(s3Utils.saveFile(image, BUCKET_NAME, USERS_FOLDER));
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
}
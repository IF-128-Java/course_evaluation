package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserProfileDtoResponse;
import ita.softserve.course_evaluation.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

	UserProfileDtoResponse readUserProfileDtoResponseById(long id);

	User readUserById(Long id);

	List<UserDto> readByFirstName(String firstName);

	void updateUser(UpdateUserDto dto, String email);

	void updatePassword(UpdatePasswordDto updatePasswordDto, String email);

	void updateUserProfilePicture(MultipartFile image, String email);

	void deleteUserProfilePicture(String email);
	
	List<UserDto> getAllStudentsByFeedbackRequestIdWithoutFeedback(long id);
}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.entity.User;

import java.util.List;

public interface UserService {

	UserDto readById(long id);

	List<UserDto> readByFirstName(String firstName);

	void updateUser(UpdateUserDto dto, String email);

	void updatePassword(UpdatePasswordDto updatePasswordDto, String email);

	String signUp(User user);
}

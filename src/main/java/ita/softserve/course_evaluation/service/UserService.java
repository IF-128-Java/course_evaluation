package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;

import java.util.List;

public interface UserService {

	UserDto readById(long id);

	List<UserDto> readByFirstName(String firstName);

	void updateUser(UpdateUserDto dto, Long userId);

	void updatePassword(UpdatePasswordDto updatePasswordDto, Long userId);

}

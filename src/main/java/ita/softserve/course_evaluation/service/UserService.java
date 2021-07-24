package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

	List<UserDto> readAll();

	UserDto readById(long id);

	UserDto readByFirstName(String firstName);

	 UserDto createUser(UserDto dto);

	UserDto updateUser(UserDto dto);

	void deleteUser(long id);

}

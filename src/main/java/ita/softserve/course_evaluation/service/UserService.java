package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

	List<User> readAll();
	Optional<User> readById(long id);
	Optional<User> readByFirstName(String firstName);
	void createUser(User user);
	void updateUser(User user);
	void deleteUser(long id);

}

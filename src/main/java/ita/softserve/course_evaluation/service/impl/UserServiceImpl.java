package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public List<User> readAll() {
		return userRepository.findAll();
	}
	
}
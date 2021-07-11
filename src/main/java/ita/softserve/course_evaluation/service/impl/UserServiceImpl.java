package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User>  readAll() {
		return userRepository.findAll();
	}

}
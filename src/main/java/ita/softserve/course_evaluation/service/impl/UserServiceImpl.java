package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User>  readAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional< User> readById(long id ) { return userRepository.findUserById( id);}

	@Override
	public Optional<User>   readByFirstName(String firstName) {return userRepository.findUserByFirstName(firstName);}

	@Override
	public void createUser(User user) {
		userRepository.save(user);
	}

	@Override
	public void updateUser(User user) {
		User daoUser = readById(user.getId()).get();
		daoUser.setFirstName(user.getFirstName());
		daoUser.setLastName(user.getLastName());
		daoUser.setEmail(user.getEmail());
		daoUser.setPassword(user.getPassword());
		daoUser.setRoles(user.getRoles());

		userRepository.save(daoUser);
	}
	@Override
	public void deleteUser(long id){
		User deletingUser = userRepository.getById(id);
		userRepository.delete(deletingUser);
		}
	}


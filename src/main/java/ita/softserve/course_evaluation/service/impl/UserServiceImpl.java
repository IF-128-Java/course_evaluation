package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
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
	public List<UserDto>  readAll() {
		return UserDtoMapper.toDto(userRepository.findAll());
	}

	@Override
	public  UserDto readById(long id ) { return UserDtoMapper.toDto(userRepository.findUserById( id).get());}

	@Override
	public UserDto   readByFirstName(String firstName) {return UserDtoMapper.toDto(userRepository.findUserByFirstName(firstName).get());}

	@Override
	public void createUser(UserDto dto) {
		userRepository.save(UserDtoMapper.fromDto(dto));
	}

	@Override
	public void updateUser(UserDto dto) {
		User daoUser = UserDtoMapper.fromDto(readById(dto.getId()));
		daoUser.setFirstName(dto.getFirstName());
		daoUser.setLastName(dto.getLastName());
		daoUser.setEmail(dto.getEmail());
		daoUser.setPassword(dto.getPassword());
		daoUser.setRoles(dto.getRoles());
		daoUser.setGroup(dto.getGroup());

		userRepository.save(daoUser);
	}
	@Override
	public void deleteUser(long id){
		User deletingUser = userRepository.getById(id);
		userRepository.delete(deletingUser);
		}
	}


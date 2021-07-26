package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
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

	public List<UserDto>  readAll() {
		return UserDtoMapper.toDto(userRepository.findAll());
	}

	@Override
	public  UserDto readById(long id ) { return UserDtoMapper.toDto(userRepository.findUserById( id).get());}

	@Override
	public UserDto   readByFirstName(String firstName) {return UserDtoMapper.toDto(userRepository.findUserByFirstName(firstName).get());}

	@Override
	public  UserDto createUser(UserDto dto) {
		return UserDtoMapper.toDto(userRepository.save(UserDtoMapper.fromDto(dto)));
	}

	@Override
	public UserDto updateUser(UserDto dto) {
		User daoUser = UserDtoMapper.fromDto(readById(dto.getId()));
		daoUser.setFirstName(dto.getFirstName());
		daoUser.setLastName(dto.getLastName());
		daoUser.setEmail(dto.getEmail());
		daoUser.setPassword(dto.getPassword());
		daoUser.setRoles(dto.getRoles());


		return UserDtoMapper.toDto(userRepository.save(daoUser));
	}
	@Override
	public void deleteUser(long id){
		User deletingUser = userRepository.getById(id);
		userRepository.delete(deletingUser);
		}
	}



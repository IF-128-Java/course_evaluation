package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class UserDtoMapper {
	
	public static UserDto toDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setRoles(user.getRoles());

		return dto;
	}
	
	public static User fromDto(UserDto dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setRoles(dto.getRoles());
        user.setGroup((dto.getGroup()));
		return user;
	}
	
	public static List<UserDto> toDto(List<User> users) {
		UserDtoMapper userDtoMapper = new UserDtoMapper();
		return Objects.isNull(users) ? null : users.stream().map(UserDtoMapper::toDto).collect(Collectors.toList());
	}
	
	public List<User> fromDto(List<UserDto> entities) {
		UserDtoMapper userDtoMapper = new UserDtoMapper();
		return Objects.isNull(entities) ? null : entities.stream().map(UserDtoMapper::fromDto).collect(Collectors.toList());
	}
	
	
}

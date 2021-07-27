package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleUserDtoMapper {
	
	public static SimpleUserDto toDto(User user) {
		SimpleUserDto dto = new SimpleUserDto();
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		return dto;
	}
	
	public static User fromDto(SimpleUserDto dto) {
		User user = new User();
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		return user;
	}
	
	public static List<SimpleUserDto> toDto(List<User> users) {
		return Objects.isNull(users) ? null : users.stream().map(SimpleUserDtoMapper::toDto).collect(Collectors.toList());
	}
	
	public List<User> fromDto(List<SimpleUserDto> dto) {
		return Objects.isNull(dto) ? null : dto.stream().map(SimpleUserDtoMapper::fromDto).collect(Collectors.toList());
	}
}

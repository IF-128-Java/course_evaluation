package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private Set<Role> roles;
	
	private Group group;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDto userDto = (UserDto) o;
		return Objects.equals(id, userDto.id) && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(password, userDto.password);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, email, password);
	}
}
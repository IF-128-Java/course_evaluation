package ita.softserve.course_evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleUserDto {
	
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	
}

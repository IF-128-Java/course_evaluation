package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserDto {
	
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	
}

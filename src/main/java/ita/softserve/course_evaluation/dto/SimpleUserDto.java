package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.validator.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class SimpleUserDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

}

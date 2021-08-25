package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.validator.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch(
        message = "Passwords value don't match!",
        field = "password",
        fieldMatch = "confirmPassword")
public class SimpleUserDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

}

package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.validator.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch(
        message = "Passwords don't match!",
        field = "password",
        fieldMatch = "confirmPassword")
public class PasswordRestoreDto {

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String token;

}

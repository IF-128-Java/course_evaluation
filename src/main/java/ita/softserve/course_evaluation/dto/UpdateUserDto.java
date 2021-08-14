package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateUserDto {

    @NotNull(message = ValidationConstants.NULL_FIRST_NAME)
    @Pattern(
            regexp = ValidationConstants.USERNAME_PATTERN,
            message = ValidationConstants.INVALID_FIRST_NAME_PATTERN
    )
    private String firstName;

    @NotNull(message = ValidationConstants.NULL_LAST_NAME)
    @Pattern(
            regexp = ValidationConstants.USERNAME_PATTERN,
            message = ValidationConstants.INVALID_LAST_NAME_PATTERN
    )
    private String lastName;
}
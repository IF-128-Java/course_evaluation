package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordDto {

    @NotBlank(message = ValidationConstants.EMPTY_OLD_PASSWORD)
    private String oldPassword;

    @NotBlank(message = ValidationConstants.EMPTY_NEW_PASSWORD)
    private String newPassword;
}
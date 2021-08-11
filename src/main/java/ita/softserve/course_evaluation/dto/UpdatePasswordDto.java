package ita.softserve.course_evaluation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordDto {

    @NotBlank(message = "Old password is mandatory!")
    private String oldPassword;

    @NotBlank(message = "New password is mandatory!")
    private String newPassword;
}
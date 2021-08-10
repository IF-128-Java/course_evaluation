package ita.softserve.course_evaluation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserDto {

    @NotBlank(message = "First name is mandatory!")
    private String firstName;

    @NotBlank(message = "Last name is mandatory!")
    private String lastName;
}
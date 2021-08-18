package ita.softserve.course_evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDtoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private byte[] profilePicture;
}
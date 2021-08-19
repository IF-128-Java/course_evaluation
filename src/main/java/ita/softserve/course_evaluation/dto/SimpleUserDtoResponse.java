package ita.softserve.course_evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleUserDtoResponse {

    private String email;
    private String firstName;
    private String lastName;
}

package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherToCourseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private Set<Role> roles;
}

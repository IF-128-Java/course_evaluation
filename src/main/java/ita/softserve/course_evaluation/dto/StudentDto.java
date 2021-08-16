package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles;
    private Long groupId;
    private String groupName;
}

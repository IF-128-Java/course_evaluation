package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private long id;

    private String courseName;

    private String description;

    private Date startDate;

    private Date endDate;

    private long teacherId;

    private String firstName;

    private String lastName;

    private Set<Role> roles;
}
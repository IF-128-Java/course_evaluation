package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    private Long id;
    private String groupName;
    private List<UserDto> students;
    private List<CourseDto> courses;

}
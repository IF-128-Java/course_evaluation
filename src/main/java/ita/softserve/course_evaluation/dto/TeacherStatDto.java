package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherStatDto {
    private Long id;
    private Long totalCourses;
    private String email;
}

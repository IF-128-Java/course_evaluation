package ita.softserve.course_evaluation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ita.softserve.course_evaluation.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private User teacher;


    @JsonProperty("teacher_id")
    private void unpackNested(Long teacher_id) {
        this.teacher = new User();
        teacher.setId(teacher_id);
    }
}
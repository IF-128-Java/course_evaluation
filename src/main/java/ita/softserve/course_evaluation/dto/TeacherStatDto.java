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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getTotalCourses() {
        return totalCourses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalCourses(Long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    @Getter @Setter
    private long id;

    @Getter @Setter
    private String courseName;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private Date startDate;

    @Getter @Setter
    private Date endDate;

    @Getter @Setter
    private User user;

    @Override
    public String toString() {
        return "CourseDto{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseDto)) return false;
        CourseDto courseDto = (CourseDto) o;
        return id == courseDto.id && courseName.equals(courseDto.courseName) && description.equals(courseDto.description) && startDate.equals(courseDto.startDate) && endDate.equals(courseDto.endDate) && user.equals(courseDto.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, description, startDate, endDate, user);
    }

}

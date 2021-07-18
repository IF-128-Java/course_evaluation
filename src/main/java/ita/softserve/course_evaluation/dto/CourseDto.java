package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class CourseDto {

    private int id;
    private String courseName;
    private String description;
    private Date startDate;
    private Date endDate;
    private User user;

    public CourseDto() {

    }

    public CourseDto(int id, String courseName, String description, Date startDate, Date endDate, User user) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

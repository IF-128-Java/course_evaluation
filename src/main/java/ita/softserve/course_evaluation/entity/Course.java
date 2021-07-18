package ita.softserve.course_evaluation.entity;

import com.sun.istack.NotNull;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(name = "course_Name", nullable = false)
    private String courseName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Course() {    }

    public Course(int id, String courseName, String description, Date startDate, Date endDate, User user) {
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
}

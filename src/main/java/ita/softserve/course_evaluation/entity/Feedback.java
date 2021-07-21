package ita.softserve.course_evaluation.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    private Integer feedbackRequest;

    public Feedback() {
    }

    public Feedback(Long id, LocalDateTime date, String comment, User student) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id) && Objects.equals(date, feedback.date) && Objects.equals(comment, feedback.comment) && Objects.equals(student, feedback.student) && Objects.equals(feedbackRequest, feedback.feedbackRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, comment, student, feedbackRequest);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", student=" + student +
                ", feedbackRequest=" + feedbackRequest +
                '}';
    }

}

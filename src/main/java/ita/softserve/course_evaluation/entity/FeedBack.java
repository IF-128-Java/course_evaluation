package ita.softserve.course_evaluation.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "course_feedback")
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_request_id")
    private FeedbackRequest feedbackRequestId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getStudentId() {
        return studentId;
    }

    public void setStudentId(User studentId) {
        this.studentId = studentId;
    }

    public FeedbackRequest getFeedbackRequestId() {
        return feedbackRequestId;
    }

    public void setFeedbackRequestId(FeedbackRequest feedbackRequestId) {
        this.feedbackRequestId = feedbackRequestId;
    }
}


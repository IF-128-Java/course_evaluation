package ita.softserve.course_evaluation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "course_feedback")
public class Feedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "date", nullable = false)
	private Timestamp date;
	
	@Column(name = "comment")
	private String comment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	private User studentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedback_request_id")
	private FeedbackRequest feedbackRequestId;
	
	public Feedback() {
	}
	
	public Feedback(Long id, Timestamp date, String comment, User studentId, FeedbackRequest feedbackRequestId) {
		this.id = id;
		this.date = date;
		this.comment = comment;
		this.studentId = studentId;
		this.feedbackRequestId = feedbackRequestId;
	}
	
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Feedback feedback = (Feedback) o;
		return Objects.equals(id, feedback.id) && Objects.equals(date, feedback.date) && Objects.equals(comment, feedback.comment) && Objects.equals(studentId, feedback.studentId) && Objects.equals(feedbackRequestId, feedback.feedbackRequestId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, date, comment, studentId, feedbackRequestId);
	}
	
	@Override
	public String toString() {
		return "Feedback{" +
				       "id=" + id +
				       ", date=" + date +
				       ", comment='" + comment + '\'' +
				       ", studentId=" + studentId +
				       ", feedbackRequestId=" + feedbackRequestId +
				       '}';
	}
}

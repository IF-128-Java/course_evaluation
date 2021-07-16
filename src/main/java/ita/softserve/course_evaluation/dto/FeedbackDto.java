package ita.softserve.course_evaluation.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class FeedbackDto {
	
	private Long id;
	private Timestamp date;
	private String comment;
	private Long studentId;
	private Long feedbackRequestId;
	
	public FeedbackDto() {
	}
	
	public FeedbackDto(Long id, Timestamp date, String comment, Long studentId, Long feedbackRequestId) {
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
	
	public Long getStudentId() {
		return studentId;
	}
	
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
	public Long getFeedbackRequestId() {
		return feedbackRequestId;
	}
	
	public void setFeedbackRequestId(Long feedbackRequestId) {
		this.feedbackRequestId = feedbackRequestId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeedbackDto that = (FeedbackDto) o;
		return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(comment, that.comment) && Objects.equals(studentId, that.studentId) && Objects.equals(feedbackRequestId, that.feedbackRequestId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, date, comment, studentId, feedbackRequestId);
	}
	
	@Override
	public String toString() {
		return "FeedbackDto{" +
				       "id=" + id +
				       ", date=" + date +
				       ", comment='" + comment + '\'' +
				       ", studentId=" + studentId +
				       ", feedbackRequestId=" + feedbackRequestId +
				       '}';
	}
}

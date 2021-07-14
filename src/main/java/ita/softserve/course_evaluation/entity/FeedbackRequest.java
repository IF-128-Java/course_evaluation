package ita.softserve.course_evaluation.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course_feedback_request")
public class FeedbackRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "feedback_description", nullable = false)
	private String feedbackDescription;
	
	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;
	
	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;
	
	@Column(name = "duration", nullable = false)
	private Long duration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id")
	private Course course;
	
	@OneToMany(mappedBy = "feedbackRequest",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<Feedback> feedbacks = new ArrayList<>();
	
	//TODO
	//@ManyToMany
	//question table
	
	
	public FeedbackRequest() {
	}
	
	public FeedbackRequest(Long id, String feedbackDescription, LocalDateTime startDate, LocalDateTime endDate, Long duration, Course course, List<Feedback> feedbacks) {
		this.id = id;
		this.feedbackDescription = feedbackDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
		this.course = course;
		this.feedbacks = feedbacks;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFeedbackDescription() {
		return feedbackDescription;
	}
	
	public void setFeedbackDescription(String feedbackDescription) {
		this.feedbackDescription = feedbackDescription;
	}
	
	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course courseId) {
		this.course = courseId;
	}
	
	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	
	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeedbackRequest that = (FeedbackRequest) o;
		return Objects.equals(id, that.id) && Objects.equals(feedbackDescription, that.feedbackDescription) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(duration, that.duration) && Objects.equals(course, that.course) && Objects.equals(feedbacks, that.feedbacks);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, feedbackDescription, startDate, endDate, duration, course, feedbacks);
	}
	
	@Override
	public String toString() {
		return "FeedbackRequest{" +
				       "id=" + id +
				       ", feedbackDescription='" + feedbackDescription + '\'' +
				       ", startDate=" + startDate +
				       ", endDate=" + endDate +
				       ", duration=" + duration +
				       ", course=" + course +
				       '}';
	}
}
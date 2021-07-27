package ita.softserve.course_evaluation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;


@Entity
@Table(name = "course_feedback_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;
	
	@OneToMany(mappedBy = "feedbackRequest",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<Feedback> feedbacks = new ArrayList<>();
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "course_feedback_request_question",
			joinColumns = @JoinColumn(name = "feedback_request_id"),
			inverseJoinColumns = @JoinColumn(name = "question_id"))
	private List<Question> questions = new ArrayList<>();
	
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


package ita.softserve.course_evaluation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	
	@Column(name = "status")
	private FeedbackRequestStatus status;
}
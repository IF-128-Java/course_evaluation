package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestDto {
	
	private Long id;
	private String feedbackDescription;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Long duration;
	private Long course;
	private List<Long> questionIds;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeedbackRequestDto that = (FeedbackRequestDto) o;
		return Objects.equals(id, that.id) && Objects.equals(feedbackDescription, that.feedbackDescription) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(duration, that.duration) && Objects.equals(course, that.course);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, feedbackDescription, startDate, endDate, duration, course);
	}
	
	@Override
	public String toString() {
		return "FeedbackRequestDto{" +
				       "id=" + id +
				       ", feedbackDescription='" + feedbackDescription + '\'' +
				       ", startDate=" + startDate +
				       ", endDate=" + endDate +
				       ", duration=" + duration +
				       ", course=" + course +
				       ", questionIds=" + questionIds +
				       '}';
	}
}

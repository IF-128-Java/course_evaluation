package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.annotation.StartDateBeforeEndDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StartDateBeforeEndDate(startDateTime = "startDate", endDateTime = "endDate")
public class FeedbackRequestDto {
	
	private Long id;
	private String feedbackDescription;
	@NotNull
	private LocalDateTime startDate;
	@NotNull
	private LocalDateTime endDate;
	@NotNull
	private Long course;
	private int status;
	private LocalDateTime lastNotification;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeedbackRequestDto that = (FeedbackRequestDto) o;
		return Objects.equals(id, that.id) && Objects.equals(feedbackDescription, that.feedbackDescription) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(course, that.course);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, feedbackDescription, startDate, endDate, course);
	}
	
	@Override
	public String toString() {
		return "FeedbackRequestDto{" +
				       "id=" + id +
				       ", feedbackDescription='" + feedbackDescription + '\'' +
				       ", startDate=" + startDate +
				       ", endDate=" + endDate +
				       ", course=" + course +
				       '}';
	}
}

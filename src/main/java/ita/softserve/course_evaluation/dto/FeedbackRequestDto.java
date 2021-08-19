package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  FeedbackRequestDto {
	
	private Long id;
	private String feedbackDescription;
	@NotNull
	@NotBlank
	private LocalDateTime startDate;
	@NotNull
	@NotBlank
	private LocalDateTime endDate;
	@NotNull
	@NotBlank
	private Long course;
	
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

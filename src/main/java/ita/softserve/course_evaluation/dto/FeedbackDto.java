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
public class FeedbackDto {
	
	private Long id;
	private LocalDateTime date;
	private String comment;
	private Long studentId;
	private Long feedbackRequestId;
	private List<AnswerDto> answers;
	
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
				       ", answers=" + answers +
				       '}';
	}
}

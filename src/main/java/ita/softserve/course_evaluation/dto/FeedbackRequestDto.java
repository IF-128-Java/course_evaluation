package ita.softserve.course_evaluation.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedbackRequestDto extends AbstractDto {
	
	private Long id;
	private String feedbackDescription;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Long duration;
	private Long courseId;
	private List<FeedbackDto> feedbacks = new ArrayList<>();
	
	public FeedbackRequestDto() {
	}
	
	public FeedbackRequestDto(Long id, String feedbackDescription, LocalDateTime startDate, LocalDateTime endDate, Long duration, Long courseId, List<FeedbackDto> feedbacks) {
		this.id = id;
		this.feedbackDescription = feedbackDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
		this.courseId = courseId;
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
	
	public Long getCourse_id() {
		return courseId;
	}
	
	public void setCourse_id(Long courseId) {
		this.courseId = courseId;
	}
	
	public List<FeedbackDto> getFeedbacks() {
		return feedbacks;
	}
	
	public void setFeedbacks(List<FeedbackDto> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeedbackRequestDto that = (FeedbackRequestDto) o;
		return Objects.equals(id, that.id) && Objects.equals(feedbackDescription, that.feedbackDescription) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(duration, that.duration) && Objects.equals(courseId, that.courseId) && Objects.equals(feedbacks, that.feedbacks);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, feedbackDescription, startDate, endDate, duration, courseId, feedbacks);
	}
	
	@Override
	public String toString() {
		return "FeedbackRequestDto{" +
				       "id=" + id +
				       ", feedbackDescription='" + feedbackDescription + '\'' +
				       ", startDate=" + startDate +
				       ", endDate=" + endDate +
				       ", duration=" + duration +
				       ", courseId=" + courseId +
				       ", feedbacks=" + feedbacks +
				       '}';
	}
}

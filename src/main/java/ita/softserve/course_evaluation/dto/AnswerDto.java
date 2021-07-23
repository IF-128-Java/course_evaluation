package ita.softserve.course_evaluation.dto;


import java.util.Objects;

public class AnswerDto {
	
	private Long rate;
	private Long questionId;
	private Long feedbackId;
	
	public AnswerDto() {
	}
	
	public AnswerDto(Long rate, Long question, Long feedback) {
		this.rate = rate;
		this.questionId = question;
		this.feedbackId = feedback;
	}
	
	public Long getRate() {
		return rate;
	}
	
	public void setRate(Long rate) {
		this.rate = rate;
	}
	
	public Long getQuestion() {
		return questionId;
	}
	
	public void setQuestion(Long question) {
		this.questionId = question;
	}
	
	public Long getFeedback() {
		return feedbackId;
	}
	
	public void setFeedback(Long feedback) {
		this.feedbackId = feedback;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AnswerDto dto = (AnswerDto) o;
		return Objects.equals(rate, dto.rate) && Objects.equals(questionId, dto.questionId) && Objects.equals(feedbackId, dto.feedbackId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(rate, questionId, feedbackId);
	}
	
	@Override
	public String toString() {
		return "AnswerDto{" +
				       ", rate=" + rate +
				       ", questionId=" + questionId +
				       ", feedbackId=" + feedbackId +
				       '}';
	}
}
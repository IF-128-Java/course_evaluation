package ita.softserve.course_evaluation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ita.softserve.course_evaluation.entity.Feedback;
import ita.softserve.course_evaluation.entity.Question;

import java.util.Objects;

public class AnswerDto {

    private Long id;
    private Integer rate;
    private Long questionId;
    private Long feedbackId;

    public AnswerDto() {
    }

    public AnswerDto(Long id, Integer rate, Long question, Long feedback) {
        this.id = id;
        this.rate = rate;
        this.questionId = question;
        this.feedbackId = feedback;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
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
        return Objects.equals(id, dto.id) && Objects.equals(rate, dto.rate) && Objects.equals(questionId, dto.questionId) && Objects.equals(feedbackId, dto.feedbackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rate, questionId, feedbackId);
    }

    @Override
    public String toString() {
        return "AnswerDto{" +
                "id=" + id +
                ", rate=" + rate +
                ", questionId=" + questionId +
                ", feedbackId=" + feedbackId +
                '}';
    }
}

package ita.softserve.course_evaluation.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.Objects;

@Entity
@Table(name = "answer_to_feedbacks")
public class AnswerToFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate")
    private Integer rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    public AnswerToFeedback() {
    }

    public AnswerToFeedback(Long id, Integer rate, Question question, Feedback feedback) {
        this.id = id;
        this.rate = rate;
        this.question = question;
        this.feedback = feedback;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerToFeedback that = (AnswerToFeedback) o;
        return Objects.equals(id, that.id) && Objects.equals(rate, that.rate) && Objects.equals(question, that.question) && Objects.equals(feedback, that.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rate, question, feedback);
    }

    @Override
    public String toString() {
        return "AnswerToFeedback{" +
                "id=" + id +
                ", rate=" + rate +
                ", question=" + question +
                ", feedback=" + feedback +
                '}';
    }
}

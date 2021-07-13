package ita.softserve.course_evaluation.entity;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "question_text")
    private String questionText;

    @Column(name="is_pattern")
    private boolean isPattern;

    @Column(name = "feedback_request_id")
    private long feedbackRequestId;


    public Question(String questionText, boolean isPattern, long feedbackRequestId) {
        this.questionText = questionText;
        this.isPattern = isPattern;
        this.feedbackRequestId = feedbackRequestId;
    }

    public Question() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public boolean getPattern() {
        return isPattern;
    }

    public void setPattern(boolean pattern) {
        isPattern = pattern;
    }

    public long getFeedbackRequestId() {
        return feedbackRequestId;
    }

    public void setFeedbackRequestId(long feedbackRequestId) {
        this.feedbackRequestId = feedbackRequestId;
    }
}

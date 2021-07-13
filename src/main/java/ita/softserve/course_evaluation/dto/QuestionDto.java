package ita.softserve.course_evaluation.dto;

public class QuestionDto {

    private long id;
    private String questionText;
    private boolean isPattern;
    private long feedbackRequestId;

    public QuestionDto(long id, String questionText, boolean isPattern, long feedbackRequestId) {
        this.id = id;
        this.questionText = questionText;
        this.isPattern = isPattern;
        this.feedbackRequestId = feedbackRequestId;
    }

    public QuestionDto() {
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

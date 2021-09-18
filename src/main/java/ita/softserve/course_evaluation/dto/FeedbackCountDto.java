package ita.softserve.course_evaluation.dto;

public class FeedbackCountDto {
    private long studentId;
    private long total;

    public long getStudentId() {
        return studentId;
    }

    public long getTotal() {
        return total;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

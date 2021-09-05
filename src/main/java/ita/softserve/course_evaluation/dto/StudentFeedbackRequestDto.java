package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentFeedbackRequestDto {

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
    private Long studentId;
}

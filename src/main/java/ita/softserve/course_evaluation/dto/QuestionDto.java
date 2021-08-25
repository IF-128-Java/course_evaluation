package ita.softserve.course_evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    private long id;
    private String questionText;
    private boolean isPattern;
}
package ita.softserve.course_evaluation.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ValidationExceptionResponse {
    @Builder.Default()
    private LocalDateTime timestamp = LocalDateTime.now();

    private String error;
    private Map<String, String> fields;
    private Integer status;
}

package ita.softserve.course_evaluation.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GenericExceptionResponse {

    @Builder.Default()
    private LocalDateTime timestamp = LocalDateTime.now();

    private String error;
    private String message;
    private Integer status;

}

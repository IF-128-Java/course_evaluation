package ita.softserve.course_evaluation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {

    private Long id;

    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String senderFirstName;

    private String senderLastName;
}
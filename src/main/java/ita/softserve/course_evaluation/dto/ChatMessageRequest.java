package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.constants.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequest {

    @Size(min = 1, max = 255, message = ValidationConstants.INVALID_MESSAGE_SIZE)
    private String content;
}
package ita.softserve.course_evaluation.dto;

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

    private LocalDateTime createdAt;

    private boolean edited;

    private Long senderId;

    private String senderFirstName;

    private String senderLastName;

    private byte[] senderProfilePicture;
}
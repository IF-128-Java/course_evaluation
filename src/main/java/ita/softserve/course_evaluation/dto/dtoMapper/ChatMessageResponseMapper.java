package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.entity.ChatMessage;
import ita.softserve.course_evaluation.service.FileManager;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ChatMessageResponseMapper {

    private final FileManager fileManager;

    public ChatMessageResponseMapper(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public ChatMessageResponse toDto(ChatMessage chatMessage){
        if (Objects.isNull(chatMessage)) return null;

        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .senderId(chatMessage.getSender().getId())
                .senderFirstName(chatMessage.getSender().getFirstName())
                .senderLastName(chatMessage.getSender().getLastName())
                .senderProfilePicture(fileManager.downloadUserProfilePicture(chatMessage.getSender().getProfilePicturePath()))
                .build();
    }

    public List<ChatMessageResponse> toDto(List<ChatMessage> chatMessages){
        return Objects.isNull(chatMessages) ? null : chatMessages.stream().map(this::toDto).collect(Collectors.toList());
    }
}
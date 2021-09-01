package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.entity.ChatMessage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatMessageResponseMapper {

    private ChatMessageResponseMapper(){}

    public static ChatMessageResponse toDto(ChatMessage chatMessage){
        if (Objects.isNull(chatMessage)) return null;

        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .senderId(chatMessage.getSender().getId())
                .senderFirstName(chatMessage.getSender().getFirstName())
                .senderLastName(chatMessage.getSender().getLastName())
                .build();
    }

    public static List<ChatMessageResponse> toDto(List<ChatMessage> chatMessages){
        return Objects.isNull(chatMessages) ? null : chatMessages.stream().map(ChatMessageResponseMapper::toDto).collect(Collectors.toList());
    }
}
package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.ChatMessageRequest;
import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatMessageService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @SneakyThrows
    @MessageMapping("/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@accessManager.isAllowedToGroupChat(#user, #chatId)")
    public void processMessage(
            @Payload @Validated ChatMessageRequest chatMessageRequest,
            @DestinationVariable Long chatId,
            @AuthenticationPrincipal SecurityUser user){

        chatMessageService.processMessage(chatMessageRequest, user, chatId);
    }

    @GetMapping("/{chatId}/all")
    @PreAuthorize("@accessManager.isAllowedToGroupChat(authentication.principal, #chatId)")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable Long chatId){

        return new ResponseEntity<>(chatMessageService.findMessagesByChatRoomId(chatId), HttpStatus.OK);
    }
}
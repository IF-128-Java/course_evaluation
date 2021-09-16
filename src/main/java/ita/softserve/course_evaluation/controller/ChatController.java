package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.ChatMessageRequest;
import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatMessageService;
import ita.softserve.course_evaluation.service.ChatRoomService;
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

@Api(tags = "Chat service REST API")
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    public ChatController(ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@accessManager.isAllowedToGroupChat(#user, #chatId) or " +
            "@accessManager.isAllowedToTeacherChat(#user, #chatId)")
    public void processCreateMessage(
            @Payload @Validated ChatMessageRequest chatMessageRequest,
            @DestinationVariable Long chatId,
            @AuthenticationPrincipal SecurityUser user){

        chatMessageService.processCreateMessage(chatMessageRequest, user, chatId);
    }

    @MessageMapping("/{chatId}/messages/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@accessManager.isAllowedToGroupChat(authentication.principal, #chatId) or " +
            "@accessManager.isAllowedToTeacherChat(authentication.principal, #chatId)")
    public void processUpdateMessage(
            @Payload @Validated ChatMessageRequest chatMessageRequest,
            @DestinationVariable Long chatId,
            @DestinationVariable Long messageId){

        chatMessageService.processUpdateMessage(chatMessageRequest, messageId);
    }

    @ApiOperation(value = "Get messages for chat")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{chatId}")
    @PreAuthorize("@accessManager.isAllowedToGroupChat(authentication.principal, #chatId) or " +
            "@accessManager.isAllowedToTeacherChat(authentication.principal, #chatId)")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable Long chatId){
        return new ResponseEntity<>(chatMessageService.findMessagesByChatRoomId(chatId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get id of a teacher chat")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = Long.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/teacher")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<Long> getTeacherChatRoomId(){
        return new ResponseEntity<>(chatRoomService.getTeacherChatRoomId(), HttpStatus.OK);
    }
}
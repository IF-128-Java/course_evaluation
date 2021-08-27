package ita.softserve.course_evaluation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ita.softserve.course_evaluation.dto.ChatMessageRequest;
import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.dto.dtoMapper.ChatMessageResponseMapper;
import ita.softserve.course_evaluation.entity.ChatMessage;
import ita.softserve.course_evaluation.entity.MessageStatus;
import ita.softserve.course_evaluation.repository.ChatMessageRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatMessageService;
import ita.softserve.course_evaluation.service.ChatRoomService;
import ita.softserve.course_evaluation.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,
                                  ChatRoomService chatRoomService,
                                  UserService userService,
                                  SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    @Override
    public void processMessage(ChatMessageRequest chatMessageRequest, SecurityUser user, Long chatId) {

        log.info("Received new message with for chat id:{} from user id:{}!", chatId, user.getId());

        ChatMessage chatMessage = ChatMessage.builder()
                .createdAt(LocalDateTime.now())
                .content(chatMessageRequest.getContent())
                .chatRoom(chatRoomService.getById(chatId))
                .sender(userService.readUserById(user.getId()))
                .status(MessageStatus.RECEIVED)
                .build();

        save(chatMessage);
        sendMessage(chatMessage);
    }

    @SneakyThrows
    private void sendMessage(ChatMessage chatMessage){
        ChatMessageResponse response = ChatMessageResponseMapper.toDto(chatMessage);

        messagingTemplate.convertAndSend("/api/v1/event/chat/" + chatMessage.getChatRoom().getId(), mapper.writeValueAsString(response));
        chatMessage.setStatus(MessageStatus.DELIVERED);
        save(chatMessage);

        log.info("Broadcast message with id {}!", chatMessage.getId());
    }

    @Override
    public void save(ChatMessage chatMessage) {
        chatMessage = chatMessageRepository.save(chatMessage);
        log.info("Saved message with id {}!", chatMessage.getId());
    }

    @Override
    public List<ChatMessageResponse> findMessagesByChatRoomId(Long chatId) {
        return ChatMessageResponseMapper.toDto(chatMessageRepository.findAllByChatRoomId(chatId));
    }
}
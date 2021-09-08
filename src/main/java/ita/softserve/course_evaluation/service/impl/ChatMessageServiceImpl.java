package ita.softserve.course_evaluation.service.impl;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageResponseMapper chatMessageResponseMapper;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,
                                  ChatRoomService chatRoomService,
                                  UserService userService,
                                  SimpMessagingTemplate messagingTemplate, ChatMessageResponseMapper chatMessageResponseMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
        this.chatMessageResponseMapper = chatMessageResponseMapper;
    }
    
    @Override
    public void processCreateMessage(ChatMessageRequest chatMessageRequest, SecurityUser user, Long chatId) {

        log.info("Received new message with chat id:{} from user id:{}!", chatId, user.getId());

        ChatMessage chatMessage = ChatMessage.builder()
                .createdAt(LocalDateTime.now())
                .content(chatMessageRequest.getContent())
                .chatRoom(chatRoomService.getById(chatId))
                .sender(userService.readUserById(user.getId()))
                .status(MessageStatus.RECEIVED)
                .build();

        save(chatMessage);
        sendMessage(chatMessageResponseMapper.toDto(chatMessage), chatId);

        chatMessage.setStatus(MessageStatus.DELIVERED);
        save(chatMessage);
    }

    @Override
    public void processUpdateMessage(ChatMessageRequest chatMessageRequest, Long messageId) {

        log.info("Received updated message with id:{}!", messageId);

        ChatMessage foundMessage = getById(messageId);

        foundMessage.setContent(chatMessageRequest.getContent());
        foundMessage.setEdited(true);

        save(foundMessage);
        sendMessage(chatMessageResponseMapper.toDto(foundMessage), foundMessage.getChatRoom().getId());
    }

    private void sendMessage(ChatMessageResponse response, Long chatRoomId){
        messagingTemplate.convertAndSend("/api/v1/event/chats/" + chatRoomId, response);
        log.info("Broadcast message with id {}!", response.getId());
    }

    @Override
    public void save(ChatMessage chatMessage) {
        chatMessage = chatMessageRepository.save(chatMessage);
        log.info("Saved message with id {}!", chatMessage.getId());
    }

    @Override
    public ChatMessage getById(Long id){
        return chatMessageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Message with id: %d not found!", id))
        );
    }

    @Override
    public List<ChatMessageResponse> findMessagesByChatRoomId(Long chatId) {
        return chatMessageResponseMapper.toDto(chatMessageRepository.findAllByChatRoomId(chatId));
    }
}
package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.ChatMessageRequest;
import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.dto.dtoMapper.ChatMessageResponseMapper;
import ita.softserve.course_evaluation.entity.ChatMessage;
import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.MessageStatus;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.ChatMessageRepository;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.impl.ChatMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.lenient;

/**
 * @author Mykhailo Fedenko on 02.09.2021
 */

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceImplTest {
    private ChatMessage chatMessage1;
    private User user1;
    private ChatRoom chatRoom1;

    @Mock private ChatMessageRepository chatMessageRepository;
    @Mock private ChatRoomService chatRoomService;
    @Mock private UserService userService;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    ChatMessageServiceImpl chatMessageService;

    @BeforeEach
    public void beforeEach(){
        user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Mike");
        user1.setLastName("Green");
        user1.setEmail("mike@mail.com");
        user1.setPassword("password");

        chatRoom1 = ChatRoom.builder()
                .id(1L)
                .chatType(ChatType.GROUP)
                .messages(Collections.emptyList())
                .build();

        chatMessage1 = ChatMessage.builder()
                .chatRoom(chatRoom1)
                .content("Test content1")
                .createdAt(LocalDateTime.now())
                .sender(user1)
                .status(MessageStatus.RECEIVED).build();
    }

    @Test
    void testProcessMessage() {
        SecurityUser securityUser = new SecurityUser(1L, "Nick", "password", Collections.emptyList(), true);
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest("Test content1");
        ChatMessageResponse response = ChatMessageResponseMapper.toDto(chatMessage1);

        when(chatRoomService.getById(Mockito.anyLong())).thenReturn(chatRoom1);
        when(userService.readUserById(Mockito.anyLong())).thenReturn(user1);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage1);
        lenient().doNothing().when(messagingTemplate).convertAndSend("/api/v1/event/chat/1", response);

        chatMessageService.processMessage(chatMessageRequest, securityUser, 1L);
        verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
        verify(userService, times(1)).readUserById(Mockito.anyLong());
        verify(chatRoomService, times(1)).getById(Mockito.anyLong());
        verify(messagingTemplate).convertAndSend(Mockito.anyString(), Mockito.any(ChatMessageResponse.class));

    }

    @Test
    void testSave() {

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage1);
        chatMessageService.save(chatMessage1);
        verify(chatMessageRepository, times(1)).save(chatMessage1);
        verifyNoMoreInteractions(chatMessageRepository);
    }

    @Test
    void testFindMessagesByChatRoomId() {
        List<ChatMessage> chatMessageList = List.of(chatMessage1);

        when(chatMessageRepository.findAllByChatRoomId(Mockito.anyLong())).thenReturn(chatMessageList);

        List<ChatMessageResponse> actual = chatMessageService.findMessagesByChatRoomId(1L);
        List<ChatMessageResponse> expected = ChatMessageResponseMapper.toDto(chatMessageList);

        assertArrayEquals(expected.toArray(), actual.toArray());

        verify(chatMessageRepository, times(1)).findAllByChatRoomId(Mockito.anyLong());
        verifyNoMoreInteractions(chatMessageRepository);
    }
}
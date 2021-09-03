package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatMessage;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.MessageStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mykhailo Fedenko
 */
@DataJpaTest
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void testFindAllByChatRoomId(){
        User user = new User();
        user.setFirstName("Mike");
        user.setLastName("Wood");
        user.setEmail("mike@com");
        user.setRoles(Set.of(Role.ROLE_TEACHER));
        user.setPassword("password");
        userRepository.save(user);

        ChatRoom chatRoom = ChatRoom.builder()
                .chatType(ChatType.GROUP)
                .messages(Collections.emptyList())
                .build();
        chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content("Test content")
                .createdAt(LocalDateTime.now())
                .sender(user)
                .status(MessageStatus.RECEIVED).build();
        chatMessageRepository.save(chatMessage);

        ChatMessage chatMessage2 = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content("Test content 2")
                .createdAt(LocalDateTime.now())
                .sender(user)
                .status(MessageStatus.RECEIVED).build();
        chatMessageRepository.save(chatMessage2);

        List<ChatMessage> actual = chatMessageRepository.findAllByChatRoomId(chatRoom.getId());
        List<ChatMessage> expected = List.of(chatMessage, chatMessage2);

        assertArrayEquals(expected.toArray(), actual.toArray());

    }

    @Test
    void testFindAllByChatRoomIdWithNotExistsId(){
        List<ChatMessage> actual = chatMessageRepository.findAllByChatRoomId(1L);
        assertEquals(0, actual.size());
    }

}

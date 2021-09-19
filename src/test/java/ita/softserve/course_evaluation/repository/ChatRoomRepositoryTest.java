package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Arsen Kushnir on 14.09.2021
 */
@DataJpaTest
public class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void testGetTeacherChatRoomIdIfExists(){
        Long expected = chatRoomRepository.save(
                ChatRoom.builder()
                        .chatType(ChatType.TEACHER)
                        .build()
        ).getId();

        Long actual = chatRoomRepository.getTeacherChatRoomId();

        assertEquals(expected, actual);
    }

    @Test
    void testGetTeacherChatRoomIdIfNotExist(){
        Long actual = chatRoomRepository.getTeacherChatRoomId();

        assertNull(actual);
    }
}
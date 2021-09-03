package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.repository.ChatRoomRepository;
import ita.softserve.course_evaluation.service.impl.ChatRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * @author Mykhailo Fedenko on 03.09.2021
 */

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

    @Mock
    ChatRoomRepository chatRoomRepository;
    @InjectMocks
    ChatRoomServiceImpl chatRoomService;

    @Test
    void testGetById() {
        ChatRoom expected = ChatRoom.builder()
                .chatType(ChatType.GROUP)
                .messages(Collections.emptyList())
                .id(1L)
                .build();
        when(chatRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

        ChatRoom actual = chatRoomService.getById(1L);

        assertEquals(expected, actual);
        verify(chatRoomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    void testGetByIdWithNotExistingChatRoom() {

        when(chatRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chatRoomService.getById(1L));

        verify(chatRoomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(chatRoomRepository);
    }
}
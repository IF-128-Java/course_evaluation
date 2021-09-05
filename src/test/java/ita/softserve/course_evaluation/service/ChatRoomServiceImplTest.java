package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.repository.ChatRoomRepository;
import ita.softserve.course_evaluation.service.impl.ChatRoomServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @AfterEach
    void afterEach(){
        verifyNoMoreInteractions(chatRoomRepository);
    }

    @Test
    void testGetByIdIfExists(){
        ChatRoom expected = ChatRoom.builder()
                .id(1L)
                .chatType(ChatType.GROUP)
                .build();

        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        ChatRoom actual = chatRoomService.getById(anyLong());

        assertEquals(expected.getId(), actual.getId());
        verify(chatRoomRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetByIdIfNotExist(){
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> chatRoomService.getById(anyLong()));

        assertEquals(String.format("ChatRoom with id: %d not found!", 0), exception.getMessage());
        verify(chatRoomRepository, times(1)).findById(anyLong());
    }
}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.repository.GroupRepository;
import ita.softserve.course_evaluation.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Arsen Kushnir on 04.09.2021
 */
@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private static Group group;

    @BeforeAll
    public static void beforeAll(){
        group = new Group();
        group.setId(1L);
    }

    @AfterEach
    void afterEach(){
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void testGetAll(){
        when(groupRepository.findAll()).thenReturn(List.of(group));

        List<GroupDto> actual = groupService.getAll();

        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
        assertEquals(group.getId(), actual.get(0).getId());

        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdIfExists(){
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

        GroupDto actual = groupService.getById(anyLong());

        assertEquals(group.getId(), actual.getId());
        verify(groupRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetByIdIfNotExist(){
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.getById(anyLong()));

        assertEquals(String.format("Group with id: %d not found!", 0), exception.getMessage());
        verify(groupRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetByChatRoomIdIfExists(){
        when(groupRepository.findByChatRoomId(anyLong())).thenReturn(Optional.of(group));

        Group actual = groupService.getByChatRoomId(anyLong());

        assertEquals(group.getId(), actual.getId());
        verify(groupRepository, times(1)).findByChatRoomId(anyLong());
    }

    @Test
    void testGetByChatRoomIdIfNotExist(){
        when(groupRepository.findByChatRoomId(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> groupService.getByChatRoomId(anyLong()));

        assertEquals(String.format("Group with ChatRoom id: %d not found!", 0), exception.getMessage());
        verify(groupRepository, times(1)).findByChatRoomId(anyLong());
    }
}
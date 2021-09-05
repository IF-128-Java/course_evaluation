package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Group;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class GroupRepositoryTests {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private ChatRoom chatRoom;
    private Group group;

    @BeforeEach
    void beforeEach(){
        chatRoom = ChatRoom.builder()
                .chatType(ChatType.GROUP)
                .build();

        group = new Group();
        group.setGroupName("Group Name");
    }

    @Test
    void testFindGroupByGroupNameIfExists(){
        group.setChatRoom(chatRoomRepository.save(chatRoom));

        Group expected = groupRepository.save(group);
        Optional<Group> actual = groupRepository.findGroupByGroupName(expected.getGroupName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void testFindGroupByGroupNameIfNotExist(){
        Optional<Group> actual = groupRepository.findGroupByGroupName(StringUtils.EMPTY);

        assertFalse(actual.isPresent());
    }

    @Test
    void testFindByChatRoomIdIfExists(){
        group.setChatRoom(chatRoomRepository.save(chatRoom));

        Group expected = groupRepository.save(group);
        Optional<Group> actual = groupRepository.findByChatRoomId(expected.getChatRoom().getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void testFindByChatRoomIdIfNotExist(){
        Optional<Group> actual = groupRepository.findByChatRoomId(0L);

        assertFalse(actual.isPresent());
    }
}
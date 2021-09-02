package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import org.apache.commons.lang3.StringUtils;
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

    @Test
    void testFindGroupByGroupNameIfExists(){
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(1L, ChatType.GROUP, null));

        Group group = new Group();
        group.setGroupName("Group Name");
        group.setChatRoom(chatRoom);

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
}
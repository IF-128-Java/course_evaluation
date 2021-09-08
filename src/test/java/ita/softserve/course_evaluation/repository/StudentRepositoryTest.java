package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentRepositoryTest {

    private User user1;
    private User user2;
    private Group groupOne;
    private Group groupTwo;
    private ChatRoom chatRoom;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void beforeEach() {

        chatRoom = new ChatRoom();
        chatRoom.setChatType(ChatType.GROUP);
        chatRoomRepository.save(chatRoom);

        groupOne = new Group();
        groupOne.setId(1l);
        groupOne.setChatRoom(chatRoom);
        groupOne.setGroupName("Group_1");
        groupRepository.save(groupOne);

        groupTwo = new Group();
        groupTwo.setId(2l);
        groupTwo.setChatRoom(chatRoom);
        groupTwo.setGroupName("Group_2");
        groupRepository.save(groupTwo);

        user1 = new User();
        user1.setId(1l);
        user1.setFirstName("Erik");
        user1.setLastName("Sparks");
        user1.setEmail("erik@testmail.com");


        user2 = new User();
        user2.setId(2l);
        user2.setFirstName("Harry");
        user2.setLastName("Sparks");
        user2.setEmail("harry@testmail.com");

    }

    @Test
    void testFindByGroupIfExists() {

        user1.setGroup(groupOne);
        userRepository.save(user1);
        user2.setGroup(groupOne);
        userRepository.save(user2);

        List<User> actual = userRepository.getStudentsByGroupId(groupOne.getId());
        List<User> expected = List.of(user1, user2);

        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
        assertArrayEquals(expected.toArray(), actual.toArray());

    }
    @Test
    void testFindByGroupIfNotExists(){

        user1.setGroup(groupOne);
        userRepository.save(user1);
        user2.setGroup(groupOne);
        userRepository.save(user2);

        List<User> actual = userRepository.getStudentsByGroupId(groupTwo.getId());

        assertTrue(actual.isEmpty());

    }

}

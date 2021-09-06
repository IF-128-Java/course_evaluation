package ita.softserve.course_evaluation.service.manager;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatRoomService;
import ita.softserve.course_evaluation.service.GroupService;
import ita.softserve.course_evaluation.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Arsen Kushnir on 04.09.2021
 */
@ExtendWith(MockitoExtension.class)
public class AccessManagerTest {

    @Mock
    private GroupService groupService;
    @Mock
    private ChatRoomService chatRoomService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AccessManager accessManager;

    private static ChatRoom chatRoom1;
    private static ChatRoom chatRoom2;
    private static Group group1;
    private static Group group2;
    private static User user;
    private static SecurityUser securityUser;

    @BeforeAll
    public static void beforeAll(){
        chatRoom1 = ChatRoom.builder()
                .id(1L)
                .chatType(ChatType.GROUP)
                .build();

        chatRoom2 = ChatRoom.builder()
                .id(2L)
                .chatType(ChatType.GROUP)
                .build();

        group1 = new Group();
        group1.setId(1L);
        group1.setChatRoom(chatRoom1);

        group2 = new Group();
        group2.setId(2L);
        group2.setChatRoom(chatRoom2);

        user = new User();
        user.setId(1L);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("email@mail.com");
        user.setPassword("1111");
        user.setRoles(Set.of(Role.ROLE_STUDENT));
        user.setGroup(group1);

        securityUser = new SecurityUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(Role::getPermissions)
                        .flatMap(Collection::stream)
                        .map(permission -> new SimpleGrantedAuthority(permission.name()))
                        .collect(Collectors.toList()),
                true);
    }

    @AfterEach
    void afterEach(){
        verifyNoMoreInteractions(groupService, chatRoomService, userService);
    }

    @Test
    void testIsAllowedToGroupChatSuccess(){
        when(chatRoomService.getById(anyLong())).thenReturn(chatRoom1);
        when(groupService.getByChatRoomId(anyLong())).thenReturn(group1);
        when(userService.readUserById(anyLong())).thenReturn(user);

        boolean actual = accessManager.isAllowedToGroupChat(securityUser, anyLong());

        assertTrue(actual);

        verify(chatRoomService, times(1)).getById(anyLong());
        verify(groupService, times(1)).getByChatRoomId(anyLong());
        verify(userService, times(1)).readUserById(anyLong());
    }

    @Test
    void testIsAllowedToGroupChatFailure(){
        when(chatRoomService.getById(anyLong())).thenReturn(chatRoom2);
        when(groupService.getByChatRoomId(anyLong())).thenReturn(group2);
        when(userService.readUserById(anyLong())).thenReturn(user);

        boolean actual = accessManager.isAllowedToGroupChat(securityUser, anyLong());

        assertFalse(actual);

        verify(chatRoomService, times(1)).getById(anyLong());
        verify(groupService, times(1)).getByChatRoomId(anyLong());
        verify(userService, times(1)).readUserById(anyLong());
    }
}
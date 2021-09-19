package ita.softserve.course_evaluation.service.manager;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.SiteNotification;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatRoomService;
import ita.softserve.course_evaluation.service.GroupService;
import ita.softserve.course_evaluation.service.SiteNotificationService;
import ita.softserve.course_evaluation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccessManager {

    private final GroupService groupService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final SiteNotificationService siteNotificationService;

    public boolean isAllowedToGroupChat(Object user, Long chatId){
        ChatRoom chatRoom = chatRoomService.getById(chatId);

        if(chatRoom.getChatType() == ChatType.GROUP){
            User userDB = userService.readUserById(((SecurityUser)user).getId());

            if(Objects.nonNull(userDB.getGroup()))
                return userDB.getGroup().getId().equals(groupService.getByChatRoomId(chatId).getId());
        }

        return false;
    }

    public boolean isAllowedToTeacherChat(Object user, Long chatId) {
        ChatRoom chatRoom = chatRoomService.getById(chatId);

        if(chatRoom.getChatType() == ChatType.TEACHER)
            return userService.readUserById(((SecurityUser)user).getId()).getRoles().contains(Role.ROLE_TEACHER);

        return false;
    }

    public boolean isAllowedToSiteNotification(Object user, Long siteNotificationId) {
       return siteNotificationService.getById(siteNotificationId).getUser().getId().equals(((SecurityUser)user).getId());
    }
}
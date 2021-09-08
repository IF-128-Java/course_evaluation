package ita.softserve.course_evaluation.service.manager;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.entity.ChatType;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.ChatMessageService;
import ita.softserve.course_evaluation.service.ChatRoomService;
import ita.softserve.course_evaluation.service.GroupService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AccessManager {

    private final GroupService groupService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;

    public AccessManager(GroupService groupService, ChatRoomService chatRoomService, UserService userService, ChatMessageService chatMessageService) {
        this.groupService = groupService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
    }

    public boolean isAllowedToGroupChat(Object user, Long chatId){
        ChatRoom chatRoom = chatRoomService.getById(chatId);

        if(chatRoom.getChatType() == ChatType.GROUP)
            return userService.readUserById(((SecurityUser)user).getId()).getGroup().getId().equals(groupService.getByChatRoomId(chatId).getId());

        return false;
    }
}
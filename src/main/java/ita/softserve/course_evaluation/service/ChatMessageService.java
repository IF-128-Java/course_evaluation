package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.ChatMessageRequest;
import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.entity.ChatMessage;
import ita.softserve.course_evaluation.entity.FeedbackRequest;
import ita.softserve.course_evaluation.security.SecurityUser;
import java.util.List;

public interface ChatMessageService {

    void processCreateMessage(ChatMessageRequest chatMessageRequest, SecurityUser user, Long chatId);

    void processUpdateMessage(ChatMessageRequest chatMessageRequest, Long messageId);

    void processCreateMessageToGroupChatAboutNewFeedbackRequest(FeedbackRequest feedbackRequest, SecurityUser user);

    void save(ChatMessage chatMessage);

    ChatMessage getById(Long id);

    List<ChatMessageResponse> findMessagesByChatRoomId(Long chatId);
}
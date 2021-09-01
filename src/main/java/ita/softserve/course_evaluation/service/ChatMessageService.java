package ita.softserve.course_evaluation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ita.softserve.course_evaluation.dto.ChatMessageRequest;
import ita.softserve.course_evaluation.dto.ChatMessageResponse;
import ita.softserve.course_evaluation.entity.ChatMessage;
import ita.softserve.course_evaluation.security.SecurityUser;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ChatMessageService {

    @Transactional
    void processMessage(ChatMessageRequest chatMessageRequest, SecurityUser user, Long chatId) throws JsonProcessingException;

    void save(ChatMessage chatMessage);

    List<ChatMessageResponse> findMessagesByChatRoomId(Long chatId);
}
package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "SELECT m.id, m.created_at, m.chat_room_id, m.user_id, m.content, m.status, " +
            "u.id, u.first_name, u.last_name, u.email, u.password, u.group_id, u.profile_picture, u.account_verified " +
            "FROM chat_messages m INNER JOIN users u ON m.user_id = u.id WHERE m.chat_room_id = ?1 ORDER BY m.created_at ASC", nativeQuery = true)
    List<ChatMessage> findAllByChatRoomId(Long chatRoomId);
}
package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT id FROM chat_rooms WHERE chat_type = 'TEACHER'", nativeQuery = true)
    Long getTeacherChatRoomId();
}

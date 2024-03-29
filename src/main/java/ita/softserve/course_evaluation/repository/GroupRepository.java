package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findGroupByGroupName(String groupName);

    @Query(value = "SELECT g.id, g.group_name, g.chat_room_id FROM groups g WHERE g.chat_room_id = ?1", nativeQuery = true)
    Optional<Group> findByChatRoomId(Long chatRoomId);

    @Query(value = "SELECT g.id, g.group_name, g.chat_room_id FROM groups g INNER JOIN course_group cg on g.id = cg.group_id\n" +
            "WHERE course_id = ?1", nativeQuery = true)
    List<Group> findGroupsByCourseId(Long courseId);
}
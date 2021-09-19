package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.SiteNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SiteNotificationRepository extends JpaRepository<SiteNotification, Long> {

    @Query(value = "SELECT * FROM site_notifications WHERE user_id = ?1", nativeQuery = true)
    List<SiteNotification> findSiteNotificationsByUserId(Long userId);
}
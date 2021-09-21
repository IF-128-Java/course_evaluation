package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation.entity.SiteNotification;
import java.util.List;

public interface SiteNotificationService {

    SiteNotification getById(Long id);

    void setReviewedSiteNotification(Long id);

    void deleteSiteNotification(Long id);

    List<SiteNotificationResponseDto> getSiteNotificationsByUserId(Long userId);
}
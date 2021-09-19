package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import java.util.List;

public interface SiteNotificationService {

    List<SiteNotificationResponseDto> getSiteNotificationsByUserId(Long userId);
}
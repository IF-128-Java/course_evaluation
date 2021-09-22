package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.SiteNotificationResponseDtoMapper;
import ita.softserve.course_evaluation.entity.SiteNotification;
import ita.softserve.course_evaluation.repository.SiteNotificationRepository;
import ita.softserve.course_evaluation.service.SiteNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteNotificationServiceImpl implements SiteNotificationService {

    private final SiteNotificationRepository siteNotificationRepository;

    @Override
    public SiteNotification getById(Long id) {
        return siteNotificationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Site notification with id: %d not found!", id))
        );
    }

    @Override
    public void setReviewedSiteNotification(Long id) {
        SiteNotification siteNotification = getById(id);

        siteNotification.setReviewed(true);
        siteNotificationRepository.save(siteNotification);
    }

    @Override
    public void deleteSiteNotification(Long id) {
        siteNotificationRepository.delete(getById(id));
    }

    @Override
    public List<SiteNotificationResponseDto> getSiteNotificationsByUserId(Long userId) {
        return SiteNotificationResponseDtoMapper.toDto(siteNotificationRepository.findSiteNotificationsByUserId(userId));
    }
}
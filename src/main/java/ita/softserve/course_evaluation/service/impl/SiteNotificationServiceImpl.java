package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.SiteNotificationResponseDtoMapper;
import ita.softserve.course_evaluation.repository.SiteNotificationRepository;
import ita.softserve.course_evaluation.service.SiteNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteNotificationServiceImpl implements SiteNotificationService {

    private final SiteNotificationRepository siteNotificationRepository;

    @Override
    public List<SiteNotificationResponseDto> getSiteNotificationsByUserId(Long userId) {
        return SiteNotificationResponseDtoMapper.toDto(siteNotificationRepository.findSiteNotificationsByUserId(userId));
    }
}
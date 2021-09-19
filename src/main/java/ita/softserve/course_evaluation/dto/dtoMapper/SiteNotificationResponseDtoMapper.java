package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation.entity.SiteNotification;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SiteNotificationResponseDtoMapper {

    private SiteNotificationResponseDtoMapper(){}

    public static SiteNotificationResponseDto toDto(SiteNotification siteNotification){
        if (Objects.isNull(siteNotification)) return null;

        return SiteNotificationResponseDto.builder()
                .id(siteNotification.getId())
                .createdAt(siteNotification.getCreatedAt())
                .header(siteNotification.getHeader())
                .content(siteNotification.getContent())
                .reviewed(siteNotification.isReviewed())
                .build();
    }

    public static List<SiteNotificationResponseDto> toDto(List<SiteNotification> siteNotifications){
        return Objects.isNull(siteNotifications) ? null : siteNotifications.stream().map(SiteNotificationResponseDtoMapper::toDto).collect(Collectors.toList());
    }
}
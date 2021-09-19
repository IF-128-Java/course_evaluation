package ita.softserve.course_evaluation.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SiteNotificationResponseDto {

    private Long id;

    private LocalDateTime createdAt;

    private String header;

    private String content;

    private boolean reviewed;
}
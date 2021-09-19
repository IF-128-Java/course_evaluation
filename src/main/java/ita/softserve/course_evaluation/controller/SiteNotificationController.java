package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.SiteNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/site_notifications")
public class SiteNotificationController {

    private final SiteNotificationService siteNotificationService;

    @GetMapping
    public ResponseEntity<List<SiteNotificationResponseDto>> getSiteNotificationsForUser(@AuthenticationPrincipal SecurityUser user){
        return new ResponseEntity<>(siteNotificationService.getSiteNotificationsByUserId(user.getId()), HttpStatus.OK);
    }
}
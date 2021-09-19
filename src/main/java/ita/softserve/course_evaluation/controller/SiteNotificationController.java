package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.SiteNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/site_notifications")
public class SiteNotificationController {

    private final SiteNotificationService siteNotificationService;

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@accessManager.isAllowedToSiteNotification(authentication.principal, #id)")
    public void setReviewedSiteNotification(@PathVariable Long id){
        siteNotificationService.setReviewedSiteNotification(id);
    }

    @GetMapping
    public ResponseEntity<List<SiteNotificationResponseDto>> getSiteNotificationsForUser(@AuthenticationPrincipal SecurityUser user){
        return new ResponseEntity<>(siteNotificationService.getSiteNotificationsByUserId(user.getId()), HttpStatus.OK);
    }
}
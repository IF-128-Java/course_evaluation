package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class testController {
    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(new User());
    }
}


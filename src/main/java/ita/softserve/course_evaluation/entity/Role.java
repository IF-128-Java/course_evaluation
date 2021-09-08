package ita.softserve.course_evaluation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
public enum Role {
    ROLE_STUDENT(Set.of(Permission.READ)),
    ROLE_TEACHER(Set.of(Permission.WRITE)),
    ROLE_ADMIN(Set.of(Permission.UPDATE)),
    ROLE_PRE_VERIFICATION(Set.of(Permission.UPDATE));

    @Getter
    private final Set<Permission> permissions;

}
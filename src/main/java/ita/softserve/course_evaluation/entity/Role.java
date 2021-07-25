package ita.softserve.course_evaluation.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ROLE_STUDENT(Set.of(Permission.FEEDBACKS_CREATE)),
    ROLE_TEACHER(Set.of(Permission.FEEDBACKS_READ)),
    ROLE_ADMIN(Set.of(Permission.FEEDBACKS_READ, Permission.FEEDBACKS_CREATE, Permission.FEEDBACKS_WRITE));
    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                       .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                       .collect(Collectors.toSet());
    }
}

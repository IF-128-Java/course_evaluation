
package ita.softserve.course_evaluation.entity;

import java.util.Set;

public enum Role {
    ROLE_STUDENT(Set.of(Permission.READ)),
    ROLE_TEACHER(Set.of(Permission.WRITE)),
    ROLE_ADMIN(Set.of(Permission.UPDATE));
    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
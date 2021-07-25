package ita.softserve.course_evaluation.entity;

public enum Permission {
    FEEDBACKS_READ("feedbacks:read"),
    FEEDBACKS_WRITE("feedbacks:write"),
    FEEDBACKS_CREATE("feedbacks:create");
    
    private final String permission;
    
    Permission(String permission) {
        this.permission = permission;
    }
    
    public String getPermission() {
        return permission;
    }

}


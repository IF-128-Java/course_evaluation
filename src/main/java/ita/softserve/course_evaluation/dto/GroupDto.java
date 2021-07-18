package ita.softserve.course_evaluation.dto;

public class GroupDto {

    private Long id;
    private String groupName;

    public GroupDto() {
    }

    public GroupDto(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

package ita.softserve.course_evaluation.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupDto groupDto = (GroupDto) o;
        return Objects.equals(id, groupDto.id) && Objects.equals(groupName, groupDto.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName);
    }

    @Override
    public String toString() {
        return "GroupDto{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}


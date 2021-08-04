package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Group;

import java.util.List;
import java.util.stream.Collectors;

public class GroupDtoMapper {

    private GroupDtoMapper(){
    }

    public static GroupDto entityToDto(Group group) {

        if (group == null) return null;

        GroupDto groupdto = new GroupDto();
        groupdto.setId(group.getId());
        groupdto.setGroupName(group.getGroupName());

        return groupdto;
    }

    public static List<GroupDto> entityToDto(List<Group> group) {

        return group == null ? null : group.stream().map(GroupDtoMapper::entityToDto).collect(Collectors.toList());
    }

    public static Group dtoToEntity(GroupDto groupdto) {

        if (groupdto == null) return null;

        Group group = new Group();
        group.setId(groupdto.getId());
        group.setGroupName(groupdto.getGroupName());

        return group;
    }

    public static List<Group> dtoToEntity(List<GroupDto> groupdto) {

        return groupdto == null ? null : groupdto.stream().map(GroupDtoMapper::dtoToEntity).collect(Collectors.toList());
    }
}

package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Group;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupDtoMapper {

    public GroupDto entityToDto(Group group) {
        GroupDto groupdto = new GroupDto();
        groupdto.setId(group.getId());
        groupdto.setGroupName(group.getGroupName());

        return groupdto;
    }

    public List<GroupDto> entityToDto(List<Group> group) {

        return group.stream().map(x -> entityToDto(x)).collect(Collectors.toList());
    }

    public Group dtoToEntity(GroupDto groupdto) {

        Group group = new Group();
        group.setId(groupdto.getId());
        group.setGroupName(groupdto.getGroupName());

        return group;
    }

    public List<Group> dtoToEntity(List<GroupDto> groupdto) {

        return groupdto.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
    }


}

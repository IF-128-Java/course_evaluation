package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.dto.dtoMapper.CourseDtoMapper;
import ita.softserve.course_evaluation.entity.Group;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GroupDtoMapper {

    private GroupDtoMapper(){
    }

    public static GroupDto entityToDto(Group group) {
        GroupDto groupdto = new GroupDto();
        groupdto.setId(group.getId());
        groupdto.setGroupName(group.getGroupName());
        groupdto.setStudents(UserDtoMapper.toDto(group.getUsers()));
        groupdto.setCourses(CourseDtoMapper.toDto(group.getCourses()));
        return groupdto;
    }

    public static List<GroupDto> entityToDto(List<Group> group) {

        return group == null ? Collections.emptyList() : group.stream().map(GroupDtoMapper::entityToDto).collect(Collectors.toList());
    }

    public static Group dtoToEntity(GroupDto groupdto) {
        Group group = new Group();
        group.setId(groupdto.getId());
        group.setGroupName(groupdto.getGroupName());
        group.setUsers(UserDtoMapper.fromDto(groupdto.getStudents()));
        group.setCourses(CourseDtoMapper.toEntity(groupdto.getCourses()));
        return group;
    }

    public static List<Group> dtoToEntity(List<GroupDto> groupdto) {

        return groupdto == null ? Collections.emptyList() : groupdto.stream().map(GroupDtoMapper::dtoToEntity).collect(Collectors.toList());
    }
}

package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.dto.GroupDtoMapper;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.repository.GroupRepository;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> getAll() {

        return GroupDtoMapper.entityToDto(groupRepository.findAll());
    }

    @Override
    public GroupDto getById(long id) {

        return GroupDtoMapper.entityToDto(groupRepository.getById(id));
    }

    @Override
    public void delete(long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Group create(GroupDto group) {

        Optional<Group> oGroup = groupRepository.findGroupByGroupName(group.getGroupName());

        if(oGroup.isEmpty()) {
            group.setId(0l);
            return groupRepository.save(GroupDtoMapper.dtoToEntity(group));
        }

        return null;
    }

    @Override
    public GroupDto update(GroupDto group) {

        Optional<Group> oGroup = groupRepository.findGroupByGroupName(group.getGroupName());

        if(oGroup.isEmpty() && groupRepository.existsById(group.getId())) {
            return GroupDtoMapper.entityToDto(groupRepository.save(GroupDtoMapper.dtoToEntity(group)));
        }

        return null;


    }
}
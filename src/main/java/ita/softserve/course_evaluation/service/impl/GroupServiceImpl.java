package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.dto.GroupDtoMapper;
import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.repository.GroupRepository;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> getAll() {
        return GroupDtoMapper.entityToDto(groupRepository.findAll());
    }

    @Override
    public GroupDto getById(long id) {
        return GroupDtoMapper.entityToDto(groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id: %d not found!", id))));
    }

    @Override
    public Group getByChatRoomId(Long id) {
        return groupRepository.findByChatRoomId(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with ChatRoom id: %d not found!", id)));
    }

    @Override
    public List<Group> getByCourseId(Long courseId){
        return groupRepository.findGroupsByCourseId(courseId);
    }
}
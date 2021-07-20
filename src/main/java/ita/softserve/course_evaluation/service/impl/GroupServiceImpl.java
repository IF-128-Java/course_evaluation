package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.repository.GroupRepository;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Group> getAll() {

        return groupRepository.findAll();
    }

    @Override
    public Group getById(long id) {

        return groupRepository.getById(id);
    }

    @Override
    public void delete(long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Group create(Group group) {

        Optional<Group> oGroup = groupRepository.findGroupByGroupName(group.getGroupName());

        if(oGroup.isEmpty()) {
            group.setId(0l);
            return groupRepository.save(group);
        }

        return null;
    }

    @Override
    public Group update(Group group) {

        Optional<Group> oGroup = groupRepository.findGroupByGroupName(group.getGroupName());

        if(oGroup.isEmpty() && groupRepository.existsById(group.getId())) {
            return groupRepository.saveAndFlush(group);
        }

        return null;


    }
}
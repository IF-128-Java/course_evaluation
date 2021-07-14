package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.repository.GroupRepository;
import ita.softserve.course_evaluation.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group getById(long id) {
        return groupRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

}
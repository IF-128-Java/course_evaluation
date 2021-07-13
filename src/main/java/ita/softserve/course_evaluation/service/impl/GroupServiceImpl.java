package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.GroupRepository;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.GroupService;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> getAllGroup() {
        return groupRepository.findAll();
    }

    @Override
    public Group readById(long id) {
       return groupRepository.getById(id);
    }

    @Override
    public void deleteGroup(long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Group addGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) {
        return groupRepository.save(group);
    }

}
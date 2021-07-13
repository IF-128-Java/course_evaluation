package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.Group;
import ita.softserve.course_evaluation.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> getAllGroup();
    Group readById(long id);
    void deleteGroup(long id);
    Group addGroup(Group group);
    Group update(Group group);
}

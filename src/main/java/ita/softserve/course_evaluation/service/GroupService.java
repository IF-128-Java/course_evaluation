package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.Group;

import java.util.List;

public interface GroupService {

    List<Group> getAll();
    Group getById(long id);
    void delete(long id);
    Group save(Group group);
}

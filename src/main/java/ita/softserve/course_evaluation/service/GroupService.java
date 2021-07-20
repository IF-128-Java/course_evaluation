package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> getAll();

    Group getById(long id);

    void delete(long id);

    Group create(Group group);

    Group update(Group group);


}

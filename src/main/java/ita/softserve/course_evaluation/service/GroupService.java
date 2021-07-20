package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.GroupDto;

import java.util.List;

public interface GroupService {

    List<GroupDto> getAll();

    GroupDto getById(long id);

    void delete(long id);

    GroupDto create(GroupDto group);

    GroupDto update(GroupDto group);


}

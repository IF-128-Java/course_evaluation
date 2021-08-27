package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.GroupDto;
import ita.softserve.course_evaluation.entity.Group;

import java.util.List;

public interface GroupService {

    List<GroupDto> getAll();

    GroupDto getById(long id);

    Group getByChatRoomId(Long id);
}
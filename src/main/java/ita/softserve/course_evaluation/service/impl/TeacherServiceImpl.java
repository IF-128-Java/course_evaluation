package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.TeacherToCourseDto;
import ita.softserve.course_evaluation.dto.dtoMapper.TeacherToCourseDtoMapper;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;

    public TeacherServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<TeacherToCourseDto> getAllTeachers() {
        List<TeacherToCourseDto> teachers = TeacherToCourseDtoMapper.toDto(userRepository.getAllTeachersByRole());
        return Objects.isNull(teachers) ? Collections.emptyList() : teachers;
    }

    @Override
    public TeacherToCourseDto getTeacherById(long id) {

        return TeacherToCourseDtoMapper.toDto(userRepository.getTeacherById(id));
    }
}

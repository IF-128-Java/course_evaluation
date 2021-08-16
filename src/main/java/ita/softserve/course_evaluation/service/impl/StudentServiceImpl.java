package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.StudentDto;
import ita.softserve.course_evaluation.dto.StudentDtoMapper;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;

    public StudentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public StudentDto getById(long id) {
        return StudentDtoMapper.toDto(userRepository.getById(id));
    }

    @Override
    public List<StudentDto> getStudentsByGroupId(long id) {
        List<StudentDto> students = StudentDtoMapper.toDto(userRepository.getStudentsByGroupId(id));
        return Objects.isNull(students) ? Collections.emptyList() : students;
    }
}

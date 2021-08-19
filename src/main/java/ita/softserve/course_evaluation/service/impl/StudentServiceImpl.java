package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.StudentDto;
import ita.softserve.course_evaluation.dto.StudentDtoMapper;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.StudentService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;

    public StudentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public StudentDto getById(long id) {
        return StudentDtoMapper.toDto(userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Student with id: %d not found!", id))));
    }

    @Override
    public List<StudentDto> getStudentsByGroupId(long id) {
        return StudentDtoMapper.toDto(userRepository.getStudentsByGroupId(id));

    }
}

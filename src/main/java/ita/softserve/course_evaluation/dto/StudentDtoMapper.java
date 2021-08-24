package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StudentDtoMapper {
    public static StudentDto toDto(User user) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(user.getId());
        studentDto.setFirstName(user.getFirstName());
        studentDto.setLastName(user.getLastName());
        studentDto.setEmail(user.getEmail());
        studentDto.setRoles(user.getRoles());

        if (user.getGroup() != null) {
            studentDto.setGroupId(user.getGroup().getId());
            studentDto.setGroupName(user.getGroup().getGroupName());
        }
        return studentDto;
    }

    public static List<StudentDto> toDto(List<User> users) {
        return Objects.isNull(users) ? Collections.emptyList() : users.stream().map(StudentDtoMapper::toDto).collect(Collectors.toList());
    }
}


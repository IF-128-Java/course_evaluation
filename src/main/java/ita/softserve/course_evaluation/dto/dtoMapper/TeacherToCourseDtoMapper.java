package ita.softserve.course_evaluation.dto.dtoMapper;

import ita.softserve.course_evaluation.dto.TeacherToCourseDto;
import ita.softserve.course_evaluation.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TeacherToCourseDtoMapper {

    public static TeacherToCourseDto toDto(User teacher) {
        if (teacher == null) return null;

        return TeacherToCourseDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .roles(teacher.getRoles())
                .build();
    }

    public static List<TeacherToCourseDto> toDto(List<User> teachers) {
        return Objects.isNull(teachers) ? null : teachers.stream().map(TeacherToCourseDtoMapper::toDto).collect(Collectors.toList());
    }

}

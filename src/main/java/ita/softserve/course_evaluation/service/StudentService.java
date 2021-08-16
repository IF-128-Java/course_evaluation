package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.StudentDto;

import java.util.List;

public interface StudentService {

    StudentDto getById(long id);

    List<StudentDto> getStudentsByGroupId(long id);
}

package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.FeedbackCountDto;
import ita.softserve.course_evaluation.dto.TeacherStatDto;
import ita.softserve.course_evaluation.dto.TeacherToCourseDto;

import java.util.List;

public interface TeacherService {

    List<TeacherToCourseDto> getAllTeachers();

    TeacherToCourseDto getTeacherById(long id);

    List<TeacherStatDto> countTotalCoursesOfTeacher();
}

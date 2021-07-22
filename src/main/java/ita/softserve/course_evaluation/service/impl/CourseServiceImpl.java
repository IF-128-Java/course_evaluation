package ita.softserve.course_evaluation.service.impl;


import ita.softserve.course_evaluation.entity.Course;
import ita.softserve.course_evaluation.repository.CourseRepository;
import ita.softserve.course_evaluation.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Transactional
    @Override
    public void addCourse() {
        Course courseJava = new Course();

    }
}

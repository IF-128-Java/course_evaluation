package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

}

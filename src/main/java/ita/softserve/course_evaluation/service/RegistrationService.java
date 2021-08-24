package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.User;

public interface RegistrationService {

    String signUp(User user);

    void enableUserEmail(String email);
}

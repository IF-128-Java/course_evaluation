package ita.softserve.course_evaluation.registration;

import ita.softserve.course_evaluation.entity.User;

public interface RegistrationService {

    String signUp(User user);

    void enableUserEmail(String email);
}

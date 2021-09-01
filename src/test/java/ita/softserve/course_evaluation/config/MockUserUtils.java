package ita.softserve.course_evaluation.config;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;

import java.util.Set;

/**
 * @author Mykhailo Fedenko on 01.09.2021
 */

public class MockUserUtils {

    private MockUserUtils(){}

    public static User getUser(WithMockCustomUser customUser) {
        Role role = customUser.role();

        User user = new User();
        user.setId(customUser.id());
        user.setRoles(Set.of(role));
        user.setEmail(customUser.email());
        user.setPassword(customUser.password());
        user.setFirstName(customUser.firstName());
        user.setLastName(customUser.lastName());

        return user;
    }

}

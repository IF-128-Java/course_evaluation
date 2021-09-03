package ita.softserve.course_evaluation.config;

import ita.softserve.course_evaluation.entity.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * @author Mykhailo Fedenko on 31.08.2021
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1;
    String firstName() default "John";
    String lastName() default "Doe";
    String email() default "john@mail.com";
    String password() default "0000";
    Role role() default Role.ROLE_STUDENT;

}

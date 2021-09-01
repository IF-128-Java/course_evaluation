package ita.softserve.course_evaluation.config;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Set;

/**
 * @author Mykhailo Fedenko on 31.08.2021
 */

@TestConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SpringSecurityTestConfiguration extends WebSecurityConfigurerAdapter {

    @Bean("withRoleAdmin")
    @Primary
    public User testUserWithRoleAdmin() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Mike");
        user.setLastName("Green");
        user.setPassword("1111");
        user.setEmail("mike@mail.com");
        user.setRoles(Set.of(Role.ROLE_ADMIN));

        return user;
    }

    @Bean("withRoleUser")
    @Primary
    public User testUserWithRoleUser() {
        User user = new User();
        user.setId(2L);
        user.setFirstName("Nick");
        user.setLastName("Brown");
        user.setPassword("1111");
        user.setEmail("nick@mail.com");
        user.setRoles(Set.of(Role.ROLE_STUDENT));

        return user;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Disable CSRF
        httpSecurity.csrf().disable();
                // Permit all requests without authentication
//                .authorizeRequests().anyRequest().permitAll();
    }

}

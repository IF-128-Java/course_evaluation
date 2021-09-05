package ita.softserve.course_evaluation.config;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


/**
 * @author Mykhailo Fedenko on 31.08.2021
 */
public class CustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {

        User user = MockUserUtils.getUser(withMockCustomUser);
        UserDetails userDetails = SecurityUser.fromUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getEmail(), userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        return context;
    }
}

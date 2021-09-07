package ita.softserve.course_evaluation.security.jwt;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.exception.JwtAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("inside JwtTokenFilter");
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            log.info("token: "+ token);
//            log.info("validate: " + jwtTokenProvider.validateToken(token));
            if (token != null && jwtTokenProvider.validateToken(token)) {
                log.info("token valid :" + token);
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                log.info("auth: " + authentication);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
			throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

package ita.softserve.course_evaluation.security.jwt;

import ita.softserve.course_evaluation.exception.handler.ExceptionHandlerFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtTokenFilter jwtTokenFilter;
	@Autowired
	private ExceptionHandlerFilter handlerFilter;

	public JwtConfigurer(JwtTokenFilter jwtTokenFilter) {
		this.jwtTokenFilter = jwtTokenFilter;
	}
	
	@Override
	public void configure(HttpSecurity httpSecurity){
		log.info("before jwt filter ");
		httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		log.info("before custom filter ");
		httpSecurity.addFilterBefore(handlerFilter,JwtTokenFilter.class);
	}

}

package ita.softserve.course_evaluation.security.jwt;

import ita.softserve.course_evaluation.exception.handler.ExceptionHandlerFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtTokenFilter jwtTokenFilter;

	private final ExceptionHandlerFilter handlerFilter;

	public JwtConfigurer(JwtTokenFilter jwtTokenFilter, ExceptionHandlerFilter handlerFilter) {
		this.jwtTokenFilter = jwtTokenFilter;
		this.handlerFilter = handlerFilter;
	}
	
	@Override
	public void configure(HttpSecurity httpSecurity){
		httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.addFilterBefore(handlerFilter,JwtTokenFilter.class);
	}
}

package ita.softserve.course_evaluation.configuration;

import ita.softserve.course_evaluation.security.jwt.JwtConfigurer;
import ita.softserve.course_evaluation.security.oauth2.CustomOAuth2UserService;
import ita.softserve.course_evaluation.security.RestAuthenticationEntryPoint;
import ita.softserve.course_evaluation.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import ita.softserve.course_evaluation.security.oauth2.OAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	public static final String LOGIN_PAGE = "/login";
	
	private final JwtConfigurer jwtConfigurer;
	@Autowired
	private UserDetailsService customUserDetailsService;

	@Autowired
	private HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

	@Autowired
	private OAuthSuccessHandler oAuthSuccessHandler;

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	public SecurityConfig(JwtConfigurer jwtConfigurer) {
		this.jwtConfigurer = jwtConfigurer;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin()
					.disable()
				.httpBasic().disable()
				.exceptionHandling()
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
				.and()
				.authorizeRequests()
					.antMatchers("/").permitAll()
					.antMatchers("/api/v1/auth/login").permitAll()
					.antMatchers("/api/v1/auth/reg").permitAll()
					.antMatchers("/v2/**", "/webjars/**","/swagger-ui/*", "/swagger-ui.html", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security").permitAll()
				.anyRequest()
					.authenticated().and()
				.oauth2Login()
					.loginPage(LOGIN_PAGE).permitAll()
					.userInfoEndpoint()
						.userService(customOAuth2UserService)
						.and()
					.authorizationEndpoint()
						.baseUri("login/oauth2/code/*")
						.authorizationRequestRepository(customAuthorizationRequestRepository())
						.and()
					.successHandler(oAuthSuccessHandler)
					.and()
					.logout()
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.GET.name()))
						.logoutSuccessUrl(LOGIN_PAGE)
						.permitAll()
					.and()
					.rememberMe();
		http.apply(jwtConfigurer);
//				.apply(jwtConfigurer);
	}
	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customAuthorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder());
	}
}

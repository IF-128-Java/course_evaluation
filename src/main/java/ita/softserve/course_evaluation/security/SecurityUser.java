package ita.softserve.course_evaluation.security;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class SecurityUser implements UserDetails, OAuth2User {

	private final Long id;
	private final String username;
	private final String password;
	private final List<SimpleGrantedAuthority> authorities;
	private final boolean isActive;
	private Map<String, Object> attributes;
	
	public SecurityUser(Long id, String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.isActive = isActive;
	}

	public SecurityUser(Long id, String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive, Map<String, Object> attributes) {
		this(id, username, password, authorities, isActive);
		this.attributes = attributes;
	}

	public static UserDetails fromUser(User user) {
		return new SecurityUser(user.getId(), user.getEmail(), user.getPassword(),
				getAuthorities(user.getRoles()), true);
	}

	public static SecurityUser create(User user) {
		List<GrantedAuthority> authorities = Collections.
				singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		return new SecurityUser(
				user.getId(),user.getEmail(),user.getPassword(),
				getAuthorities(user.getRoles()), true);
	}

	public static SecurityUser create(User user, Map<String, Object> attributes) {
		SecurityUser securityUser = SecurityUser.create(user);
		securityUser.setAttributes(attributes);
		return securityUser;
	}
	
	private static List<SimpleGrantedAuthority> getAuthorities(Set<Role> roles){
		return roles.stream().map(Role::getPermissions).flatMap(Collection::stream)
				.map(permission -> new SimpleGrantedAuthority(permission.name()))
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return isActive;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return isActive;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return isActive;
	}
	
	@Override
	public boolean isEnabled() {
		return isActive;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}
}

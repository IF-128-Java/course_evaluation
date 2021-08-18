package ita.softserve.course_evaluation.security;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.registration.ActivaUserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class SecurityUser implements UserDetails {

	private final Long id;
	private final String username;
	private final String password;
	private final List<SimpleGrantedAuthority> authorities;
	private final boolean isActive;

	public SecurityUser(Long id, String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.isActive = isActive;
	}

	public static UserDetails fromUser(User user, boolean isActive) {

		return new SecurityUser(user.getId(), user.getEmail(), user.getPassword(),
				getAuthorities(user.getRoles()), isActive);
	}
	
	private static List<SimpleGrantedAuthority> getAuthorities(Set<Role> roles){
		return roles.stream().map(Role::getPermissions).flatMap(Collection::stream)
				.map(permission -> new SimpleGrantedAuthority(permission.name()))
				.collect(Collectors.toList());
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

}

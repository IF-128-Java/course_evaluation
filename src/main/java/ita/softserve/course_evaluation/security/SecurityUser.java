package ita.softserve.course_evaluation.security;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SecurityUser implements UserDetails {
	
	private final String username;
	private final String password;
	private final List<SimpleGrantedAuthority> authorities;
	private final boolean isActive;
	
	public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.isActive = isActive;
	}
	
	public static UserDetails fromUser(User user) {
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), true, true, true,
				true, getAuthorities(user.getRoles()));
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

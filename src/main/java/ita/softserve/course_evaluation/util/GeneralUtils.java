package ita.softserve.course_evaluation.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import ita.softserve.course_evaluation.security.oauth2.users.SocialProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



public class GeneralUtils {

//	public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
//		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//		for (Role role : roles) {
//			authorities.add(new SimpleGrantedAuthority(role.getName()));
//		}
//		return authorities;
//	}

	public static SocialProvider toSocialProvider(String providerId) {
		for (SocialProvider socialProvider : SocialProvider.values()) {
			if (socialProvider.getProviderType().equals(providerId)) {
				return socialProvider;
			}
		}
		return SocialProvider.LOCAL;
	}

//	public static UserInfo buildUserInfo(LocalUser localUser) {
//		List<String> roles = localUser.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
//		User user = localUser.getUser();
//		return new UserInfo(user.getId().toString(), user.getDisplayName(), user.getEmail(), roles);
//	}
}

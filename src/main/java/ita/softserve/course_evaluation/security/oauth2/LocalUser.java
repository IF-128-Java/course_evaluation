package ita.softserve.course_evaluation.security.oauth2;

import ita.softserve.course_evaluation.security.SecurityUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


public class LocalUser extends User implements OidcUser, OAuth2User {

    private static final long serialVersionUID = -2845160792248762779L;
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;
    private Map<String, Object> attributes;
    private ita.softserve.course_evaluation.entity.User user;


    public LocalUser(final String userID, final String password,
                     final boolean enabled, final boolean accountNonExpired,
                     final boolean credentialsNonExpired,
                     final boolean accountNonLocked,
                     final Collection<? extends GrantedAuthority> authorities,
                     final ita.softserve.course_evaluation.entity.User user) {
        this(userID, password, enabled,
                accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities, user,
                null, null);
    }

    public LocalUser(final String userID, final String password,
                     final boolean enabled, final boolean accountNonExpired,
                     final boolean credentialsNonExpired,
                     final boolean accountNonLocked,
                     final Collection<? extends GrantedAuthority> authorities,
                     final ita.softserve.course_evaluation.entity.User user,
                     OidcIdToken idToken, OidcUserInfo userInfo) {
        super(userID, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    public static LocalUser create(ita.softserve.course_evaluation.entity.User user,
                                   Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        user.setAccountVerified(true);
        LocalUser localUser = new LocalUser(user.getEmail(), user.getPassword(), true, true, true, true,
                SecurityUser.fromUser(user).getAuthorities(), user, idToken, userInfo);
        localUser.setAttributes(attributes);
        return localUser;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.user.getFirstName() + " " + this.user.getLastName();
    }

    public ita.softserve.course_evaluation.entity.User getUser() {
        return user;
    }
}

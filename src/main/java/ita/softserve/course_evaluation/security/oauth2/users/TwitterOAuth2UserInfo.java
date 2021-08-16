package ita.softserve.course_evaluation.security.oauth2.users;

import java.util.Map;

public class TwitterOAuth2UserInfo extends OAuth2UserInfo{
    public TwitterOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

}

package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.security.oauth2.users.SocialProvider;
import ita.softserve.course_evaluation.validator.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
@Builder
@AllArgsConstructor
public class SignUpRequest {

    private Long userID;

    private String providerUserId;

    @NotEmpty
    private String displayName;

    @NotEmpty
    private String email;

    private SocialProvider socialProvider;

    @Size(min = 6, message = "{Size.userDto.password}")
    private String password;

    @NotEmpty
    private String matchingPassword;

    public SignUpRequest(String providerUserId, String displayName, String email, String password, SocialProvider socialProvider) {
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.socialProvider = socialProvider;
    }
}
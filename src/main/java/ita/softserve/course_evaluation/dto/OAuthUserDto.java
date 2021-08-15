package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserDto {

    private Long id;

    private String providerId;

    private String email;

    private String enabled;

    private String displayName;

    private Date createdDate;

    private Date modifiedDate;

    private String Password;

    private String provider;

    private Set<Role> roles;

}

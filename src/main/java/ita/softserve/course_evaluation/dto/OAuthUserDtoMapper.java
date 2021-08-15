package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
public class OAuthUserDtoMapper {

   public static User fromDto(OAuthUserDto dto) {
       return new User(dto.getId(),
               dto.getDisplayName().split(" ")[0],
               dto.getDisplayName().split(" ")[1], dto.getEmail(), dto.getPassword());
   }

   public static OAuthUserDto toDto(User user) {
       return OAuthUserDto.builder().id(user.getId())
               .displayName(user.getFirstName() + " " + user.getLastName())
               .email(user.getEmail())
               .Password(user.getPassword())
               .roles(user.getRoles()).build();
   }

}

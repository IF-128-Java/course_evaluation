package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class UserDtoMapper {

        private static ModelMapper mapper;

    public static UserDto toDto(User user) {
        return Objects.isNull(user) ? null : mapper.map(user, UserDto.class);
        }

    public static User fromDto (UserDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, User.class);
        }

    public static List<UserDto> toDto(List<User> users) {
        UserDtoMapper userDtoMapper = new UserDtoMapper();
        return Objects.isNull(users) ? null : users.stream().map(UserDtoMapper::toDto).collect(Collectors.toList());
        }

    public List<User> fromDto (List<UserDto> entities) {
    UserDtoMapper userDtoMapper = new UserDtoMapper();
      return Objects.isNull(entities) ? null: entities.stream().map(UserDtoMapper::fromDto).collect(Collectors.toList());
    }


}

package ita.softserve.course_evaluation.dto;

import ita.softserve.course_evaluation.entity.User;

public class SimpleUserDtoResponseMapper {

    private SimpleUserDtoResponseMapper(){}

    public static SimpleUserDtoResponse toDto(User user){
        return SimpleUserDtoResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .secret(user.getSecret())
                .build();
    }
}
